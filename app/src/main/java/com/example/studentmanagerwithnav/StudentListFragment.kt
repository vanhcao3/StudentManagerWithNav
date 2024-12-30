package com.example.studentmanagerwithnav

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studentmanagerwithnav.StudentModel

class StudentListFragment : Fragment() {

    private lateinit var studentList: MutableList<StudentModel>
    private lateinit var adapter: ArrayAdapter<StudentModel>
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)
        setHasOptionsMenu(true)

        // Gắn danh sách sinh viên
        studentList = mutableListOf(
            StudentModel("Nguyễn Văn A", "SV001"),
            StudentModel("Trần Thị B", "SV002")
        )
        listView = view.findViewById(R.id.list_view_students)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, studentList)
        listView.adapter = adapter

        // Đăng ký Context Menu
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
                // Điều hướng tới AddEditStudentFragment với đối tượng student
                val action = StudentListFragmentDirections.actionStudentListFragmentToAddEditStudentFragment(student)
                findNavController().navigate(action)
                return true
            }
            R.id.menu_remove -> {
                studentList.removeAt(info.position)
                adapter.notifyDataSetChanged()
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

        // Lắng nghe thêm mới sinh viên
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<StudentModel>("student_add")
            ?.observe(viewLifecycleOwner) { newStudent ->
                studentList.add(newStudent)
                adapter.notifyDataSetChanged()
            }

        // Lắng nghe cập nhật sinh viên
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<StudentModel>("student_update")
            ?.observe(viewLifecycleOwner) { updatedStudent ->
                val index = studentList.indexOfFirst { it.id == updatedStudent.id }
                if (index != -1) {
                    studentList[index] = updatedStudent
                    adapter.notifyDataSetChanged()
                }
            }
    }

}
