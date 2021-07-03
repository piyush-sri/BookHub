package com.piyush.bookhub.adapter


import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.piyush.bookhub.R
import com.piyush.bookhub.activity.DescriptionActivity
import com.piyush.bookhub.adapter.Database.BookEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_favourite_single_row.*

class FavouriteRecyclerAdapter(val context:Context,val bookList:List<BookEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
      return bookList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
       val book = bookList[position]

        holder.textBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.defaultimg).into(holder.imgBookImage)

       holder.llcontent.setOnClickListener{
           var intent =Intent(context,DescriptionActivity::class.java)
           intent.putExtra("book_id",book.book_id.toString())// to string use because when we save the book to database the id of the book is saved as int ,but description activity needs string value
         //converts the id into string
           context.startActivity(intent)
       }

    }

    class FavouriteViewHolder(view:View): RecyclerView.ViewHolder(view){
        val textBookName : TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtBookAuthor : TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice : TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating : TextView = view.findViewById(R.id.txtFavBookRating)
        val imgBookImage : ImageView= view.findViewById(R.id.imgFavBookImage)
        val llcontent: LinearLayout = view.findViewById(R.id.llFavContent)


    }
}
