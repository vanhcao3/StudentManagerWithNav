package com.example.studentmanagerwithnav

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.studentmanagerwithnav.StudentModel

class StudentListFragment : Fragment() {

    private lateinit var studentList: List<StudentModel>
    private lateinit var adapter: ArrayAdapter<StudentModel>
    private lateinit var listView: ListView
    private lateinit var viewModel: StudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)
        setHasOptionsMenu(true)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
        
        listView = view.findViewById(R.id.list_view_students)
        studentList = emptyList() // Initialize empty list
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, studentList.toMutableList())
        listView.adapter = adapter

        // Observe students from database
        viewModel.allStudents.observe(viewLifecycleOwner) { students ->
            studentList = students
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, students)
            listView.adapter = adapter
        }

        // Register Context Menu
        registerForContextMenu(listView)

        return view
    }

    // Context Menu
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val student = studentList[info.position]

        when (item.itemId) {
            R.id.menu_edit -> {
                val action = StudentListFragmentDirections.actionStudentListFragmentToAddEditStudentFragment(student)
                findNavController().navigate(action)
                return true
            }
            R.id.menu_remove -> {
                viewModel.delete(student)
                Toast.makeText(requireContext(), "${student.name} đã bị xóa", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    // Option Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_new -> {
                // Truyền null khi thêm mới
                val action = StudentListFragmentDirections
                    .actionStudentListFragmentToAddEditStudentFragment(null)
                findNavController().navigate(action)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listen for new student
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<StudentModel>("student_add")
            ?.observe(viewLifecycleOwner) { newStudent ->
                viewModel.insert(newStudent)
            }

        // Listen for student updates
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<StudentModel>("student_update")
            ?.observe(viewLifecycleOwner) { updatedStudent ->
                viewModel.update(updatedStudent)
            }
    }

}
