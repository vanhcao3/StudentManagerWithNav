package com.example.studentmanagerwithnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AddEditStudentFragment : Fragment() {

    private var student: StudentModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit_student, container, false)

        // Lấy đối số từ Navigation Component
        arguments?.let {
            student = AddEditStudentFragmentArgs.fromBundle(it).student
        }

        // Hiển thị thông tin sinh viên nếu đang chỉnh sửa
        val editTextName = view.findViewById<EditText>(R.id.et_name)
        val editTextId = view.findViewById<EditText>(R.id.et_student_id)
        val buttonSave = view.findViewById<Button>(R.id.btn_save)

        student?.let {
            editTextName.setText(it.name)
            editTextId.setText(it.id)
        }

        // Lưu sinh viên
        buttonSave.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val studentId = editTextId.text.toString().trim()

            // Kiểm tra đầu vào
            if (name.isEmpty() || studentId.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tạo đối tượng sinh viên mới
            val newStudent = StudentModel(name, studentId)

            // Trả dữ liệu về StudentListFragment
            val navController = findNavController()

            if (student == null) {
                // Thêm mới
                navController.previousBackStackEntry?.savedStateHandle?.set("student_add", newStudent)
                Toast.makeText(requireContext(), "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show()
            } else {
                // Cập nhật sinh viên
                navController.previousBackStackEntry?.savedStateHandle?.set("student_update", newStudent)
                Toast.makeText(requireContext(), "Cập nhật sinh viên thành công!", Toast.LENGTH_SHORT).show()
            }

            // Quay lại màn hình danh sách
            navController.popBackStack()
        }

        return view
    }


}

