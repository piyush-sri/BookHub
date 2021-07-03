package com.piyush.bookhub.activity

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.piyush.bookhub.R
import com.piyush.bookhub.adapter.Database.BookDatabase
import com.piyush.bookhub.adapter.Database.BookEntity
import com.piyush.bookhub.model.Book
import com.piyush.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {
    //declaring the variables
    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar


    var bookId: String? = "100"
    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        //initializing the variables that we have declared earlier by findViewById()method
        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progrssBar)
        progressBar.visibility =
            View.VISIBLE// to show the progress Bar and layout when the activity opens till the data is fetched
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected Error Occurred",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected Error Occurred",
                Toast.LENGTH_SHORT
            ).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                    Response.Listener {
                        try {
                            val success = it.getBoolean("success")
                            if (success) {
                                val bookJsonObject = it.getJSONObject("book_data")
                                progressLayout.visibility = View.GONE
                                val bookImageUrl =
                                    bookJsonObject.getString("image")  // u just missed this line
                                // parsing the json according to the response
                                Picasso.get().load(bookJsonObject.getString("image"))
                                    .error(R.drawable.defaultimg).into(imgBookImage)
                                txtBookName.text = bookJsonObject.getString("name")
                                txtBookAuthor.text = bookJsonObject.getString("author")
                                txtBookPrice.text = bookJsonObject.getString("price")
                                txtBookRating.text = bookJsonObject.getString("rating")
                                txtBookDesc.text = bookJsonObject.getString("description")

                                val bookEntity = BookEntity(
                                    bookId?.toInt() as Int,
                                    txtBookName.text.toString(),
                                    txtBookAuthor.text.toString(),
                                    txtBookPrice.text.toString(),
                                    txtBookRating.text.toString(),
                                    txtBookDesc.text.toString(),
                                    bookImageUrl
                                )

                                val checkFav =
                                    DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                val isFav = checkFav.get()

                                if (isFav) {
                                    btnAddToFav.text = "Remove from Favourites"
                                    val favColor = ContextCompat.getColor(
                                        applicationContext,
                                        R.color.colorFavourite
                                    )
                                    btnAddToFav.setBackgroundColor(favColor)
                                } else {
                                    btnAddToFav.text = "Add to Favourites"
                                    val noFavColor = ContextCompat.getColor(
                                        applicationContext,
                                        R.color.colorPrimary
                                    )
                                    btnAddToFav.setBackgroundColor(noFavColor)
                                }

                                btnAddToFav.setOnClickListener {

                                    if (!DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                            .get()
                                    ) {
                                           val async   =
                                            DBAsyncTask(applicationContext, bookEntity, 2).execute()


                                        val result:Boolean = async.get()
                                        if (result) {
                                            Toast.makeText(
                                                this@DescriptionActivity,
                                                "Book added to favourites",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            btnAddToFav.text = "Remove from favourites"
                                            val favColor = ContextCompat.getColor(
                                                applicationContext,
                                                R.color.colorFavourite
                                            )
                                            btnAddToFav.setBackgroundColor(favColor)

                                        }else{
                                            Toast.makeText(
                                                this@DescriptionActivity,
                                                "Some Error Occurred!!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }else{
                                        val async =DBAsyncTask(applicationContext,bookEntity,3).execute()
                                        val result= async.get()
                                        if(result){
                                            Toast.makeText(
                                                this@DescriptionActivity,
                                                "Book removed from favourites",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            btnAddToFav.text ="Add to Favourites"
                                            val noFavColor= ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                            btnAddToFav.setBackgroundColor(noFavColor)
                                        }else{
                                            Toast.makeText(this@DescriptionActivity,
                                            "Some error occurred!",
                                            Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                }


                            }
                        else {
                                Toast.makeText(
                                    this@DescriptionActivity,
                                    "Some Error Occurred!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } catch (e: Exception) {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some Erro Occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },

                    Response.ErrorListener {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Volley Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()


                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "be320cdc164167"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("ok") { text,
                                             listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)// this lines help us close the app from any point

            }
            dialog.create()
            dialog.show()
        }


    }
    class DBAsyncTask(val context :Context,val bookEntity: BookEntity,val mode: Int) : AsyncTask<Void,Void,Boolean>(){

        /*
        * Mode1 : check the db if the book is favourite or not
        * Mode2: save the book into db as favourite
        * Mode3: remove the favourite book
        * */

        val db = Room.databaseBuilder(context,BookDatabase::class.java,"book-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
             when(mode){
                 1-> {
                     val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                     db.close()
                     return book!=null
                 }
                 2->{
                     db.bookDao().insertBook(bookEntity)
                     db.close()
                     return true
                 }
                 3->{
                     db.bookDao().deleteBook(bookEntity)
                     db.close()
                     return true
                 }
             }

             return false
        }

    }
}