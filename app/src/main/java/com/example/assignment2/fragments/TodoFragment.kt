package com.example.assignment2.fragments


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2.R
import com.example.assignment2.adapters.WarehouseAdapter
import com.example.assignment2.main.WarehouseApp
import com.example.assignment2.todo.Todo
import com.example.assignment2.todo.TodoAdapter

import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_stock.view.*
import kotlinx.android.synthetic.main.item_todo.view.*


class TodoFragment : Fragment() {

    private lateinit var todoAdapter: TodoAdapter
    lateinit var app: WarehouseApp
    lateinit var loader : AlertDialog
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as WarehouseApp


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.fragment_main, container, false)
        activity?.title = getString(R.string.action_todo)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))




        btnAddTodo.setOnClickListener {
            val todoTitle = etTodoTitle.text.toString()
            if(todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                etTodoTitle.text.clear()
            }
        }
        btnDeleteDoneTodos.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }
        return(root)
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            TodoFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}