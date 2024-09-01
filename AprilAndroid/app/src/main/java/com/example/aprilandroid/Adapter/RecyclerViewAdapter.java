//package com.example.aprilandroid.Adapter;
//
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.aprilandroid.Model.Student;
//import com.example.aprilandroid.R;
//
//import java.util.List;
//
//public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
//
////    String[] countries;
//    List<Student> studentList;
//
////    public RecyclerViewAdapter(String[] countries){
////
////        this.countries = countries;
////    }
//
//    public RecyclerViewAdapter(List<Student> studentList) {
//        this.studentList = studentList;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
//        return new RecyclerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//        Student student = studentList.get(position);
//        holder.name.setText(student.getName());
////        holder.name.setText(countries[position]);
//        holder.itemView.setOnClickListener(v -> {
////        holder.name.setOnClickListener(v -> {
////            Toast.makeText(v.getContext(), countries[position]+ " Clicked", Toast.LENGTH_SHORT).show();
//            Toast.makeText(v.getContext(), student.getName()+ " Clicked", Toast.LENGTH_SHORT).show();
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//
//        return studentList.size();
//    }
//
//    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
//
//        TextView name;
//
//        public RecyclerViewHolder(@NonNull View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.name);
//        }
//    }
//}
