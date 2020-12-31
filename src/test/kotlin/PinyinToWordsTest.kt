import com.yff.ptw.PinyinToWords
import org.junit.Test

class PinyinToWordsTest  {

    @Test
    fun initTest(){
        PinyinToWords.init()
    }

    @Test
    fun pinyinTest(){
        PinyinToWords.init()
        assert(PinyinToWords.findWords("nihao").first().zh == "你好")
        assert(PinyinToWords.findWords("women").first().zh == "我们")
        assert(PinyinToWords.findWords("tianqi").first().zh == "天气")
        assert(PinyinToWords.findWords("riben").first().zh == "日本")
        assert(PinyinToWords.findWords("zhonghuarenmingongheguo").first().zh == "中华人民共和国")
    }
}