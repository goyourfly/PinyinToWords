import com.yff.ptw.PinyinToWords
import com.yff.ptw.obj.CacheType
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class PinyinToWordsTest  {
    @Test
    fun test(){
        val cachePath = File(Paths.get("").toAbsolutePath().toFile(),"cache")
        PinyinToWords.init(cachePath,CacheType.TYPE_PROTO_BUF)
        assert(PinyinToWords.findWords("nihao").first().zh == "你好")
        assert(PinyinToWords.findWords("women").first().zh == "我们")
        assert(PinyinToWords.findWords("tianqi").first().zh == "天气")
        assert(PinyinToWords.findWords("riben").first().zh == "日本")
        assert(PinyinToWords.findWords("zhonghuarenmingongheguo").first().zh == "中华人民共和国")
    }
}