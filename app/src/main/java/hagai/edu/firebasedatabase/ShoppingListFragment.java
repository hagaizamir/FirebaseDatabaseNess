package hagai.edu.firebasedatabase;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hagai.edu.firebasedatabase.dialogs.AddListFragment;
import hagai.edu.firebasedatabase.dialogs.ShareFragment;
import hagai.edu.firebasedatabase.models.ShoppingLists;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {
    @BindView(R.id.fabAddList)
    FloatingActionButton fabAddList;
    @BindView(R.id.rvShoppingLists)
    RecyclerView rvShoppingLists;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        //1) query the data for the view
        //1.1) get the user.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return view; //no user -> no user Lists.

        //1.2) get the ref to the user-table
        DatabaseReference ref =
                FirebaseDatabase.getInstance().getReference("UserLists").child(user.getUid());
        //2) init a ShoppingListAdapter
        ShoppingListAdapter adapter = new ShoppingListAdapter(ref, this);

        //3) set The LayoutManager and adapter of the recyclerView
        rvShoppingLists.setAdapter(adapter);
        rvShoppingLists.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabAddList)
    public void onFabAddListClicked() {
        AddListFragment dialog = new AddListFragment();
        dialog.show(getChildFragmentManager(), "AddListDialog");
    }


    public static class ShoppingListAdapter extends FirebaseRecyclerAdapter<ShoppingLists, ShoppingListAdapter.ShoppingListViewHolder>{
        Fragment fragment;
        public ShoppingListAdapter(Query query, Fragment fragment) {
            super(ShoppingLists.class,
                    R.layout.shopping_list_name_item,
                    ShoppingListViewHolder.class,
                    query
            );
            this.fragment = fragment;
        }

        @Override
        protected void populateViewHolder(ShoppingListViewHolder viewHolder, ShoppingLists model, int position) {
            viewHolder.tvListName.setText(model.getName());
            viewHolder.model = model;
        }

        @Override
        public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

            return new ShoppingListViewHolder(view, fragment);
        }

        public static class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvListName;
            FloatingActionButton fabListShare;
            Fragment fragment;
            ShoppingLists model;

            public ShoppingListViewHolder(View itemView, Fragment fragment) {
                super(itemView);
                this.fragment = fragment;
                tvListName = (TextView) itemView.findViewById(R.id.tvListName);
                fabListShare = (FloatingActionButton) itemView.findViewById(R.id.fabListShare);

                fabListShare.setOnClickListener(this);
                itemView.setOnClickListener(this);
            }
            @Override
            public void onClick(View v) {
                if (v == fabListShare) {
                    //1) instance of the userDialog fragment.
                    ShareFragment shareFragment = ShareFragment.newInstance(model);

                    //2) instance.show(fm /*childFragmentManager*/, "tag")
                    shareFragment.show(fragment.getChildFragmentManager(), "ShareFragment");
                } else {

                    //listid->
                    ShoppingListItemsFragment shoppingListItemsFragment = new ShoppingListItemsFragment();
                    //put the list as an extra... (newInstance())
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("list", model);
                    shoppingListItemsFragment.setArguments(bundle);


                 //   shoppingListItemsFragment.show(fragment.getChildFragmentManager(), "items");

                    fragment.getActivity().getSupportFragmentManager().
                            beginTransaction().
                            addToBackStack("shoppingListItemsFragment").
                            replace(R.id.container, shoppingListItemsFragment).
                            commit();
                }
            }
        }
    }
}
