package hagai.edu.firebasedatabase.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hagai.edu.firebasedatabase.R;
import hagai.edu.firebasedatabase.models.ShoppingLists;
import hagai.edu.firebasedatabase.models.User;

/**

 */
public class ShareFragment extends BottomSheetDialogFragment {
    private static final String ARG_MODEL = "model";

    //@BindString(R.string.app_name) String appName;

    @BindView(R.id.rvUsers)
    RecyclerView rvUsers;
    Unbinder unbinder;
    ShoppingLists model;

    //save the data to the bundle setArguments.
    public static ShareFragment newInstance(ShoppingLists model) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MODEL, model);
        ShareFragment fragment = new ShareFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        unbinder = ButterKnife.bind(this, view);
        //String appName = getResources().getString(R.string.app_name);
        //get the arguments from the bundle (newInstance)
        model = getArguments().getParcelable(ARG_MODEL);

        Query ref = FirebaseDatabase.getInstance().getReference("Users");

        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUsers.setAdapter(new UserAdapter(ref, model, this));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static class UserAdapter extends FirebaseRecyclerAdapter<User, UserAdapter.UserViewHolder>{
        ShoppingLists shoppingList = null;
        Fragment fragment;

        public UserAdapter(Query query, ShoppingLists shoppingList, Fragment fragment) {
            super(User.class, R.layout.user_item, UserViewHolder.class, query);
            this.shoppingList = shoppingList;
            this.fragment = fragment;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new UserViewHolder(view, fragment);
        }

        @Override
        protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
            Context context = viewHolder.ivUserImage.getContext();
            viewHolder.tvUserName.setText(model.getDisplayName());
            Glide.with(context).load(model.getProfileImage()).into(viewHolder.ivUserImage);

            viewHolder.user = model;
            viewHolder.shoppingLists = shoppingList;
        }

        public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CircularImageView ivUserImage;
            TextView tvUserName;
            User user;
            ShoppingLists shoppingLists;
            Fragment fragment;

            public UserViewHolder(View itemView, Fragment fragment) {
                super(itemView);
                this.fragment = fragment;
                ivUserImage = (CircularImageView) itemView.findViewById(R.id.ivUserImage);
                tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(final View v) {
                //get the model-> save it under the uid selected user
                //save the shopping list to the db under uid of the selected user:

                //1)get a ref to UserLists.child(selectedUser.uid).child(list.listID)
                DatabaseReference ref = FirebaseDatabase.
                        getInstance().
                        getReference("UserLists").
                        child(user.getUid()).
                        child(shoppingLists.getListUID());

                ref.setValue(shoppingLists).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(v.getContext(), "Shared", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(v.getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                        }
                        if (fragment instanceof DialogFragment)
                            ((DialogFragment) fragment).dismiss();
                    }
                });

            }
        }

    }

}
