# PinyinToWords
[![](https://jitpack.io/v/goyourfly/PinyinToWords.svg)](https://jitpack.io/#goyourfly/PinyinToWords)

根据拼音查找对应的词条，类似于词典或输入法功能

### 性能
- 单次查询耗时 0.2ms 左右

### 使用方式
- Gradle

  ```
  // Step 1. Add the JitPack repository to your build file
  // Add it in your root build.gradle at the end of repositories:
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
  // Step 2. Add the dependency
  implementation 'com.github.goyourfly:PinyinToWords:VERSION'
  ```

- 初始化词库，首次初始化会从词库读取拼音并生成字典树，耗时 5s 左右，后续将字典树缓存后耗时 500ms 左右
  ```
  PinyinToWords.init()
  ```
- 查找词条
  ```
  // [Word(pinyin=nihao, zh=你好, freq=34121)]
  PinyinToWords.findWords("nihao")
  // [Word(pinyin=shenme, zh=什么, freq=1041223), Word(pinyin=shenme, zh=甚么, freq=4729), Word(pinyin=shenme, zh=神么, freq=127)]
  PinyinToWords.findWords("shenme")
  ```
- 自定义词库
  ```
  // 重写 PathProvider
  interface PathProvider{
    // 词库目录
    fun getDictPath():String
    // 数据缓存目录，目前只用于字典树缓存
    fun getCacheDir():String
  }
  
  // 重写 WordsParser 用于将词库的每行数据解析为 Word 对象
  interface WordsParser {
    fun parse(line: String): Word
  }
  ```
  
### 实现方式
- 字典树 + 随机读
  
### 感谢
- 词库是从 [webdict](https://github.com/ling0322/webdict) 获取，共 201195 个词条，感谢大佬，原词库没有拼音，用 [TinyPinyin](https://github.com/promeG/TinyPinyin) 生成
