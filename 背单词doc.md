### 



## 前端设计

#### 核心组件实现

#### 单词卡片组件

```vue

```

<script>
export default {
  props: {
    wordData: {
      type: Object,
      required: true
    }
  },
  methods: {
    playPronunciation() {
      this.$emit('play-audio', this.wordData.word_name)
    },
    handleCardClick() {
      this.$router.push(`/word-detail/${this.wordData.id}`)
    }
  }
}
</script>

<script>
export default {
  props: {
    wordData: {
      type: Object,
      required: true
    }
  },
  methods: {
    playPronunciation() {
      this.$emit('play-audio', this.wordData.word_name)
    },
    handleCardClick() {
      this.$router.push(`/word-detail/${this.wordData.id}`)
    }
  }
}
</script>

#### 输入验证

function validateSpelling(input, correctWord) {
  const diffAnalysis = []
  const maxLength = Math.max(input.length, correctWord.length)

  for (let i = 0; i < maxLength; i++) {
    const status = 
      i >= input.length ? 'missing' :
      i >= correctWord.length ? 'extra' :
      input[i] === correctWord[i] ? 'correct' : 'wrong'

    diffAnalysis.push({
      position: i + 1,
      inputChar: input[i] || '',
      correctChar: correctWord[i] || '',
      status
    })

  }

  return {
    isCorrect: input.toLowerCase() === correctWord.toLowerCase(),
    diffAnalysis,
    accuracy: Math.round(
      (diffAnalysis.filter(d => d.status === 'correct').length / correctWord.length) * 100
    )
  }
}

#### 智能默写系统

// 动态难度调整算法
function adjustDifficulty(history) {
  const { accuracy, responseTimes } = history
  const avgAccuracy = accuracy.reduce((a,b) => a+b, 0) / accuracy.length
  const avgTime = responseTimes.reduce((a,b) => a+b, 0) / responseTimes.length

  return {
    timeLimit: Math.max(15, 60 - avgTime * 0.8),
    wordCount: Math.min(50, Math.floor(20 * (1 + (avgAccuracy - 0.7) * 2))),
    hintLevel: avgAccuracy < 0.6 ? 'high' : avgAccuracy < 0.8 ? 'medium' : 'low'
  }
}



#### 语音评测系统

// 语音录制与评测
async function startRecording() {
  const recorder = wx.getRecorderManager()
  recorder.onStart(() => console.log('录音开始'))
  recorder.onStop(async (res) => {
    const { tempFilePath } = res
    const evaluation = await evaluatePronunciation(tempFilePath, currentWord)
    this.updatePronunciationScore(evaluation.score)
  })

  recorder.start({
    format: 'mp3',
    sampleRate: 16000,
    numberOfChannels: 1
  })
}

## 

## 后端设计与核心接口设计

#### 表结构规范

-- 用户表
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  openid VARCHAR(255) UNIQUE NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  daily_goal INT DEFAULT 20,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_login TIMESTAMP 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 单词表（含JSON字段扩展）
CREATE TABLE words (
  id INT PRIMARY KEY AUTO_INCREMENT,
  word VARCHAR(100) UNIQUE NOT NULL,
  pronunciation VARCHAR(100),
  meaning TEXT NOT NULL,
  category ENUM('basic','core','advanced') NOT NULL,
  tags JSON,
  difficulty TINYINT DEFAULT 1,
  example TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 学习记录表（含间隔重复字段）
CREATE TABLE learning_records (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  word_id INT NOT NULL,
  next_review DATE NOT NULL,
  easiness FLOAT DEFAULT 2.5,
  repetitions INT DEFAULT 0,
  interval INT DEFAULT 1,
  last_review TIMESTAMP,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_word FOREIGN KEY (word_id) REFERENCES words(id),
  INDEX idx_user_word (user_id, word_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



#### 单词相关接口

// 获取智能推荐单词
GET /api/words/smart-recommend
Params:

- category: 单词分类
- difficulty: 难度等级
  Response:
  {
  "new_words": [
  {
    "id": 101,
    "word": "algorithm",
    "pronunciation": "[ˈælɡərɪðəm]",
    "meaning": "n.算法"
  }
  ],
  "review_words": [
  {
    "id": 205,
    "word": "variable",
    "pronunciation": "[ˈveəriəbl]",
    "meaning": "n.变量"
  }
  ]
  }

// 提交学习结果
POST /api/learn/record
Headers:
  Authorization: Bearer <token>
Body:
{
  "word_id": 101,
  "result": "correct",
  "response_time": 4.2,
  "mistake_type": "spelling"
}
Response:
{
  "code": 0,
  "message": "学习记录已保存",
  "next_review": "2023-10-20"
}



#### 单词推荐服务

// WordRecommendationService.java
public List<Word> recommendWords(User user) {
    // 1. 获取用户学习记录
    List<LearningRecord> records = recordRepo.findByUserId(user.getId());

    // 2. 应用间隔重复算法
    List<Word> dueWords = records.stream()
        .filter(record -> record.getNextReview().before(new Date()))
        .sorted(Comparator.comparing(LearningRecord::getEasiness))
        .map(LearningRecord::getWord)
        .limit(10)
        .collect(Collectors.toList());
    
    // 3. 混合新词
    List<Word> newWords = wordRepo.findNewWords(
        user.getLevel(), 
        dueWords.size() < 5 ? 10 - dueWords.size() : 3
    );
    
    // 4. 动态混合
    return mixWords(dueWords, newWords, user.getLearningStyle());

}

private List<Word> mixWords(List<Word> review, List<Word> news, String style) {
    // 根据用户学习风格采用不同混合策略
    if ("visual".equals(style)) {
        return IntStream.range(0, Math.max(review.size(), news.size()))
            .mapToObj(i -> {
                if (i % 3 == 0 && i/3 < news.size()) return news.get(i/3);
                if (i % 3 != 0 && (i-1)/3 < review.size()) return review.get((i-1)/3);
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    // 其他混合策略...
}



#### 学习分析服务

# analysis_service.py

class LearningAnalyzer:
    def __init__(self, user_id):
        self.user_id = user_id
        self.records = self._load_records()

    def get_mistake_patterns(self):
        # 使用聚类分析错误模式
        mistakes = [r for r in self.records if not r.correct]
        vectors = [self._extract_features(m) for m in mistakes]
    
        kmeans = KMeans(n_clusters=3)
        clusters = kmeans.fit_predict(vectors)
    
        return {
            'spelling': self._count_cluster(clusters, 0),
            'meaning': self._count_cluster(clusters, 1),
            'grammar': self._count_cluster(clusters, 2)
        }
    
    def _extract_features(self, record):
        # 特征工程：提取错误特征
        return [
            len(record.word),
            record.response_time,
            int(' ' in record.user_answer),
            # 其他特征...
        ]

## 测试方案

#### 单元测试示例

describe('SM-2算法实现', () => {
  const testCases = [
    {
      input: { easiness: 2.5, interval: 1, repetitions: 0, performance: 3 },
      expected: { easiness: 2.5, interval: 1 }
    },
    {
      input: { easiness: 2.5, interval: 6, repetitions: 2, performance: 4 },
      expected: { easiness: 2.6, interval: 15 }
    }
  ]

  testCases.forEach(({ input, expected }) => {
    it(`性能评级${input.performance}时应返回间隔${expected.interval}天`, () => {
      const result = calculateNextReview(input)
      assert.equal(result.interval, expected.interval)
      assert.closeTo(result.easiness, expected.easiness, 0.1)
    })
  })
})



#### 集成测试示例

describe('完整学习流程', () => {
  let testUser
  let authToken

  before(async () => {
    // 创建测试用户
    testUser = await User.create({
      openid: 'test_openid_123',
      nickname: '测试用户'
    })

    // 获取认证令牌
    const res = await chai.request(app)
      .post('/api/auth/login')
      .send({ code: 'test_wechat_code' })
    authToken = res.body.token

  })

  it('应完成从获取单词到提交结果的完整流程', async () => {
    // 获取推荐单词
    const res1 = await chai.request(app)
      .get('/api/words/smart-recommend')
      .set('Authorization', `Bearer ${authToken}`)

    // 提交学习结果
    const res2 = await chai.request(app)
      .post('/api/learn/record')
      .set('Authorization', `Bearer ${authToken}`)
      .send({
        word_id: res1.body.new_words[0].id,
        result: 'correct',
        response_time: 3.5
      })
    
    // 验证学习记录
    const record = await LearningRecord.findOne({
      where: { user_id: testUser.id }
    })
    
    expect(res2).to.have.status(200)
    expect(record).to.not.be.null
    expect(record.next_review).to.be.a('Date')

  })
})



#### Mock策略与部署方案

// tests/mocks/wechat.js
jest.mock('wx-server-sdk', () => ({
  cloud: {
    init: jest.fn(),
    callWXAPI: jest.fn(({ API_NAME }) => {
      if (API_NAME === 'login') {
        return Promise.resolve({
          openid: 'mock_openid_123',
          session_key: 'mock_session_key'
        })
      }
      return Promise.reject('未知API调用')
    })
  }
}))



FROM node:16-alpine as builder

WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:1.21-alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]



scrape_configs:

- job_name: 'node_app'
  metrics_path: '/metrics'
  static_configs:
  
  - targets: ['app:3000']
    relabel_configs:
  - source_labels: [__address__]
    target_label: instance
    replacement: 'production_app'

- job_name: 'mysql'
  static_configs:
  
  - targets: ['mysql:9104']
