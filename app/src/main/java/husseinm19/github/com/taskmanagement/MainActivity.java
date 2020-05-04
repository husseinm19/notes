package husseinm19.github.com.taskmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import husseinm19.github.com.taskmanagement.model.Data;

/**
 * Created by Hussein on 24-04-2020
 */

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fat_Btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    ImageView deleteImage;


    //RecycleView
    private RecyclerView recyclerView;

    //updateData
    EditText titleupd ;
    EditText noteeupd;

    Button saveUpdate ;
    Button delete;

    //variable for update
    private String title;
    private String note;
    private String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Loading Progress Bar
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kitMain);
        Sprite doubleBounce = new DoubleBounce();
        Sprite cubeGrid = new CubeGrid();
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       progressBar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        }, 2000);

        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" My Notes   ");

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();

         String uID = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uID);
        mDatabase.keepSynced(true);


        //Recycle View Read Data

        recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);





        fat_Btn = findViewById(R.id.action_bar_button);

        fat_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View myView = inflater.inflate(R.layout.custom_input_field, null);
                myDialog.setView(myView);
                final AlertDialog dialog = myDialog.create();

                final EditText title = (EditText) myView.findViewById(R.id.edt_title);
                final EditText note = (EditText) myView.findViewById(R.id.edt_note);

                Button saveButton = (Button) myView.findViewById(R.id.save_btn);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mTitle = title.getText().toString().trim();
                        String mNote = note.getText().toString().trim();

                        if (TextUtils.isEmpty(mTitle) && (TextUtils.isEmpty(mNote))) {
                            title.setError("Required Filed");
                            note.setError("Required Filed");
                            return;

                        }

                        if (TextUtils.isEmpty(mTitle)) {
                            title.setError("Required Field");
                            return;
                        }
                        if (TextUtils.isEmpty(mNote)) {
                            note.setError("Required Field");
                            return;
                        }

                        String id = mDatabase.push().getKey();
                        String Date = DateFormat.getDateInstance().format(new Date());

                        Data data = new Data(mTitle, mNote, Date, id);

                        mDatabase.child(id).setValue(data);
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    }
                });

                dialog.show();


            }
        });

    }

    //update database
    public void updateData(){
        AlertDialog.Builder updateDialog=new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater =LayoutInflater.from(MainActivity.this);
        View updateView = inflater.inflate(R.layout.update_input_field,null);
        updateDialog.setView(updateView);
        final AlertDialog dialog=updateDialog.create();



         titleupd =updateView.findViewById(R.id.edt_titleUpd);
         noteeupd =  updateView.findViewById(R.id.edt_noteUpd);

         titleupd.setText(title);
         titleupd.setSelection(title.length());

         noteeupd.setText(note);
         noteeupd.setSelection(note.length());

         saveUpdate =  updateView.findViewById(R.id.save_btnUpd);
         delete =  updateView.findViewById(R.id.delete_btn);



        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=titleupd.getText().toString().trim();
                note=noteeupd.getText().toString().trim();
                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data = new Data(title,note,mDate,post_key);

                mDatabase.child(post_key).setValue(data);

                Toast.makeText(MainActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();
                Toast.makeText(MainActivity.this, "Deleted!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>(
                Data.class,
                R.layout.item_data,
                MyViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setNotes(model.getNote());
                viewHolder.setDate(model.getDate());

//                deleteImage=findViewById(R.id.delete_image);
//                deleteImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(MainActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
//                    }
//                });



                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key=getRef(position).getKey();
                        title=model.getTitle();
                        note=model.getNote();
                        updateData();
                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView=itemView;

        }

        public void setTitle(String title){
            TextView mTitle = myView.findViewById(R.id.txt_title);
            mTitle.setText(title);
        }

        public void setNotes(String note){
            TextView mNotes = myView.findViewById(R.id.txt_note);
            mNotes.setText(note);
        }

        public void setDate(String date){
            TextView mDate = myView.findViewById(R.id.txt_date);
            mDate.setText(date);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();

    }
}
