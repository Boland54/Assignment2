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
import com.example.assignment2.models.WarehouseModel
import com.example.assignment2.todo.Todo
import com.example.assignment2.todo.TodoAdapter
import com.example.assignment2.todo.TodoListener
import kotlinx.android.synthetic.main.card_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_stock.view.*
import kotlinx.android.synthetic.main.fragment_todo.*
import kotlinx.android.synthetic.main.fragment_todo.view.*
import kotlinx.android.synthetic.main.item_todo.view.*


class TodoFragment : Fragment() , TodoListener {

    lateinit var todoAdapter: TodoAdapter
    lateinit var app: WarehouseApp
    lateinit var loader : AlertDialog
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as WarehouseApp


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.fragment_todo, container, false)
        activity?.title = getString(R.string.action_todo)

        root.rvTodoItems.setLayoutManager(LinearLayoutManager(activity))

        setButtonListener(root)
        setDeleteButtonListener(root)
        return root;

    }

    override fun onTodoClick(todo: Todo) {
        activity!!.supportFragmentManager.beginTransaction()
               // .replace(R.id.homeFrame, TodoFragment.newInstance(todo))
                .addToBackStack(null)
                .commit()
    }


    fun setButtonListener( layout: View) {
        layout.btnAddTodo.setOnClickListener {
            val todoTitle = etTodoTitle.text.toString()
            if(todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                etTodoTitle.text.clear()
            }
        }
    }

    fun setDeleteButtonListener( layout: View) {
        layout.btnDeleteDoneTodos.setOnClickListener {
                todoAdapter.deleteDoneTodos()

        }
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            TodoFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}