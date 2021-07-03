package com.piyush.bookhub.fragment


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.piyush.bookhub.R
import com.piyush.bookhub.adapter.Database.BookDatabase
import com.piyush.bookhub.adapter.Database.BookEntity
import com.piyush.bookhub.adapter.FavouriteRecyclerAdapter

/**
 * A simple [Fragment] subclass.
 */
class FavouritesFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter:FavouriteRecyclerAdapter
    var dbBookList = listOf<BookEntity>()






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view=  inflater.inflate(R.layout.fragment_favourites,container,false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progrssBar)

        layoutManager = GridLayoutManager(activity as Context,2)

        dbBookList = RetrieveFavourites(activity as Context).execute().get()

        //check that the activity holding this adapter is not empty and the list too is not empty
        if (activity!=null){
            progressLayout.visibility = View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavourite.layoutManager = layoutManager
            recyclerFavourite.adapter = recyclerAdapter


        }



        return  view
    }

    class RetrieveFavourites(val context: Context): AsyncTask<Void,Void,List<BookEntity>>(){

        override fun doInBackground(vararg params: Void?): List<BookEntity> {
             val db = Room.databaseBuilder(context,BookDatabase::class.java,"book-db").build()
            return db.bookDao().getAllBooks()
        }

    }

}
