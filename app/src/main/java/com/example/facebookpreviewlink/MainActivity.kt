package com.example.facebookpreviewlink

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var buttonPost: Button
    private lateinit var imageViewProfile: ShapeableImageView
    private lateinit var editText: EditText
    private lateinit var linearLayoutPost: LinearLayout
    private lateinit var imageViewPost: ImageView
    private lateinit var buttonCloseImageView: ImageView
    private lateinit var linearLayoutBottomPost: LinearLayout
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView

    companion object {
        private const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }

    private fun initViews() {
        buttonPost = findViewById(R.id.btn_post)
        imageViewProfile = findViewById(R.id.image_view_profile)
        editText = findViewById(R.id.edittext_post)
        linearLayoutPost = findViewById(R.id.linear_layout_post)
        imageViewPost = findViewById(R.id.image_view_post)
        buttonCloseImageView = findViewById(R.id.btn_close_image)
        linearLayoutBottomPost = findViewById(R.id.linear_layout_bottom_post)
        textView1 = findViewById(R.id.text_view_1)
        textView2 = findViewById(R.id.text_view_2)

        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG, "onTextChanged: ${p0.toString()}")
                MyTask(p0.toString()).execute()
            }
        })
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask(private val url: String) :
        AsyncTask<Void?, Void?, String?>() {

        private var title: String? = null
        private var desc: String? = null
        private var image: String? = null
        private var baseUrl: String? = null

        override fun doInBackground(vararg p0: Void?): String? {

            val doc: Document

            try {
                doc = Jsoup.connect(this.url).get()

                val metaPropertiesOgTags = doc.select("meta[property^=og:]")
                metaPropertiesOgTags.forEachIndexed { index, _ ->
                    val tag = metaPropertiesOgTags[index]
                    when (tag.attr("property")) {
                        "og:image" -> image = tag.attr("content")
                        "og:description" -> desc = tag.attr("content")
                        //   "og:url" -> url = tag.attr("content")
                        "og:title" -> title = tag.attr("content")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Log.d(
                TAG,
                "doInBackground: image = $image \n desc = $desc \n url = $baseUrl title = $title"
            )
            return null
        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {
            //if you had a ui element, you could display the title
        }
    }
}
