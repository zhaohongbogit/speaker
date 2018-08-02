package tk.hongbo.speaker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy
import com.baidu.tts.client.SpeechError
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    var TAG: String = MainActivity::class.java.simpleName

    val mSpeechSynthesizer: SpeechSynthesizer = SpeechSynthesizer.getInstance()
    var message: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSpeaker()

        button.setOnClickListener {
            initPermission()
        }
    }

    fun initSpeaker() {
        mSpeechSynthesizer.setContext(this)
        mSpeechSynthesizer.setSpeechSynthesizerListener(SpeechSysListener())
        LoggerProxy.printable(true)

        mSpeechSynthesizer.setAppId("11618296")
        mSpeechSynthesizer.setApiKey("mTrHOudqxkOZQEsGVd5rE322", "CawYwfi7LtzwBqkj7C8w6guLl2Q5zQln")

        //设置离在线混合模式
        mSpeechSynthesizer.auth(TtsMode.MIX)

        //设置合成参数
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0")

        //初始化合成引擎
        mSpeechSynthesizer.initTts(TtsMode.MIX)
    }

    fun speakHello() {
        if (!TextUtils.isEmpty(editText.text)) {
            message = editText.text.toString()
        }
        mSpeechSynthesizer.speak(message)
//        mSpeechSynthesizer.synthesize("泓博先生")
    }

    inner class SpeechSysListener : SpeechSynthesizerListener {
        override fun onSynthesizeStart(p0: String?) {
            //播放开始，每句播放开始都会回调
//            Log.d(this@MainActivity.TAG, "onSynthesizeStart")
        }

        override fun onSpeechFinish(p0: String?) {
            //播放结束回调
//            Log.d(this@MainActivity.TAG, "onSpeechFinish")
        }

        override fun onSpeechProgressChanged(p0: String?, p1: Int) {
            //播放进度回调接口，分多次回调
//            Log.d(this@MainActivity.TAG, "onSpeechFinish")
        }

        override fun onSynthesizeFinish(p0: String?) {
            //合成正常结束，每句合成正常结束都会回调，如果过程中出错，则回调onError，不再回调此接口
//            Log.d(this@MainActivity.TAG, "onSpeechFinish")
        }

        override fun onSpeechStart(p0: String?) {
            //播放开始
//            Log.d(this@MainActivity.TAG, "onSpeechFinish")
        }

        override fun onSynthesizeDataArrived(p0: String?, p1: ByteArray?, p2: Int) {
            //语音流 16K采样率 16bits编码 单声道 。合成进度
//            Log.d(this@MainActivity.TAG, "onSpeechFinish")
        }

        override fun onError(p0: String?, p1: SpeechError?) {
            //当合成或者播放过程中出错时回调此接口
//            Log.d(this@MainActivity.TAG, "onSpeechFinish")
        }

    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private fun initPermission() {
        val permissions = arrayOf<String>(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE)

        val toApplyList = ArrayList<String>()

        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm)
                //进入到这里代表没有权限.
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        speakHello()
    }
}
