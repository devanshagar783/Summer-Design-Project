package com.sdp.remotehealthcareapp.Fragments.Home;

import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sdp.remotehealthcareapp.Fragments.HealthFiles.Medical;
import com.sdp.remotehealthcareapp.Fragments.HealthFiles.MyProfile;
import com.sdp.remotehealthcareapp.R;

import org.w3c.dom.Text;

import java.util.Objects;

public class Tab3 extends Fragment {

    private static final String TAG = "Tab3";

    View v;
    private CircularImageView imageView;
    private TextView userName;
    private TextView userEmail;
    private TextView userNum;
    private FloatingActionButton fab;
    private Button invite;

    public Tab3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tab3, container, false);
        userName = (TextView) v.findViewById(R.id.username);
        userEmail = (TextView) v.findViewById(R.id.useremail);
        userNum = (TextView) v.findViewById(R.id.usernum);
        fab = (FloatingActionButton) v.findViewById(R.id.fabedit);
        invite = (Button) v.findViewById(R.id.inviteuser);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                String name = (document.getString("Name"));
                                String num = (document.getString("number"));
                                if (document.getString("Email") != null)
                                    userEmail.setText(document.getString("Email"));
                                else
                                    userEmail.setVisibility(View.GONE);
                                userName.setText(name);
                                userNum.setText(num);
                            }
                        }
                    });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new MyProfile()).addToBackStack("Profile Setup").commit();
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateContentLink();
            }
        });

        return v;
    }

    public void generateContentLink() {
        Uri baseUrl = Uri.parse("https://remotehealthcareproject.page.link/invite");
        String message = "Hey there! Try this amazing healthcare application Hygeia and install it from here:-" + baseUrl.toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Refer Application"));
    }
}