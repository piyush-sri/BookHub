package com.piyush.bookhub.adapter

import java.util.*
import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.piyush.bookhub.R
import com.piyush.bookhub.activity.DescriptionActivity
import com.piyush.bookhub.model.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.view.*
import org.w3c.dom.Text

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return itemList.size //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        // val text =itemList.get(position)// they use itemList(position)
        //holder.textView.text=text//To change body of created functions use File | Settings | File Templates.
        val book = itemList[position]
        holder.txtBookName.text=book.bookName
        holder.textBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        //holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.defaultimg).into(holder.imgBookImage)//here the error method help us load the default image of the imageview if any error occured

        //this is the place we add click listeners to our views
        holder.llContent.setOnClickListener{
            //Toast.makeText(context,"Clicked On ${holder.txtBookName.text}",Toast.LENGTH_SHORT).show()
         val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)

        }
    }

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val textView: TextView = view.findViewById(R.id.txtBookName)
        val txtBookName: TextView = view.findViewById(R.id.txtBookName)
        val textBookAuthor : TextView = view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice: TextView =view.findViewById(R.id.txtBookPrice)
        val txtBookRating : TextView= view.findViewById(R.id.txtBookRating)
        val imgBookImage : ImageView =view.findViewById(R.id.imgBookImage)
        val llContent: LinearLayout   = view.findViewById(R.id.llContent)// this is the place where all our views are initialized
    }
}