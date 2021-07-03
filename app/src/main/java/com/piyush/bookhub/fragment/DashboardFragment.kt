package com.piyush.bookhub.fragment


import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.piyush.bookhub.R
import com.piyush.bookhub.adapter.DashboardRecyclerAdapter
import com.piyush.bookhub.model.Book
import com.piyush.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

   /* lateinit var btnCheckInternet: Button*/

    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    /* val bookList = arrayListOf(
         "P.S.I Still Love You",
         "The Great Gateby",
         "Anna Karenina",
         "Madame Bovary",
         "War and Peace",
         "Lolita",
         "Middlemarch",
         "The Adventure of Huckleberry Finn",
         "Moby-Dick",
         "The Lord of the Rings"
     )*/
    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    val bookInfoList = arrayListOf<Book>()

/*    Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5",R.drawable.ps_ily  ),
    Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby ),
    Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare  ),
    Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame  ),
    Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace  ),
    Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita  ),
    Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch  ),
    Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn  ),
    Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick  ),
    Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings  ))*/

    val ratingComparator = Comparator<Book>{
        book1,book2 ->
        //if the book has same rating then they must sorted alphabetically
        if(book1.bookRating.compareTo(book2.bookRating,true)==0){
            book1.bookName.compareTo(book2.bookName,true)// to compare two strings
        }
        else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)// used only for adding a menu to a fragment

       // btnCheckInternet = view.findViewById(R.id.btnCheckInternet)
        // initialization of the button
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progrssBar)
        progressLayout.visibility=View.VISIBLE
        // adding click listener to the button that we have created in dasdboard fragment
       /* btnCheckInternet.setOnClickListener {
            if (ConnectionManager().checkConnectivity(activity as Context)) {
                // Internet is available for this we add a dialog box here
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("ok") { text, listener -> }
                dialog.setNegativeButton("Cancel") { text, listener -> }
                dialog.create()
                dialog.show()
            } else {
                // Internet is not available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection not Found")
                dialog.setPositiveButton("ok") { text, listener -> }
                dialog.setNegativeButton("Cancel") { text, listener -> }
                dialog.create()
                dialog.show()

            }
        }*/
        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    // here we will handle the response

                    try {

                        progressLayout.visibility =View.GONE//this will hide the progrss Bar when the data comes
                        val success = it.getBoolean("success")
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)

                                recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

                                recyclerAdapter = DashboardRecyclerAdapter(
                                    activity as Context,
                                    bookInfoList
                                ) //as operator is used for type casting


                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager =
                                    layoutManager  // these two lines are used to initialize the adapter and layout manager and attachthem with the recyclerView

                               /* recyclerDashboard.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerDashboard.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )*/

                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occured!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {
                    // here we will handle the errors
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error occured!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {  // the code to send the header content
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "be320cdc164167"
                        return headers
                    }


                }


            queue.add(jsonObjectRequest)
        } else { //if no internet connection found that either the user go to setting or to exit the app
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("ok") { text,
                                             listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)// this lines help us close the app from any point

            }
            dialog.create()
            dialog.show()

        }
        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater?.inflate(R.menu.menu_dashbard , menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id =item?.itemId
        if(id == R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)// sort list in decreasing order
            bookInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()


        return super.onOptionsItemSelected(item)
    }

}
