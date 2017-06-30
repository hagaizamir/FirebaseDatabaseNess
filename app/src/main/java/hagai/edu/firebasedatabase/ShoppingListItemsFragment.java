package hagai.edu.firebasedatabase;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import hagai.edu.firebasedatabase.models.ShoppingListProduct;
import hagai.edu.firebasedatabase.models.ShoppingLists;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListItemsFragment extends DialogFragment {
    ShoppingLists shoppingList;
    @BindView(R.id.fabAddProduct)
    FloatingActionButton fabAddProduct;
    @BindView(R.id.etProductName)
    EditText etProductName;
    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list_items, container, false);
        unbinder = ButterKnife.bind(this, view);
        shoppingList = getArguments().getParcelable("list");

        //1) ref list items.
        DatabaseReference ref = FirebaseDatabase.
                getInstance().
                getReference("ListItems").
                child(shoppingList.getListUID());

        ProductAdapter adapter = new ProductAdapter(ref);
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabAddProduct)
    public void onViewClicked() {
        //1) ref list items.
        DatabaseReference ref = FirebaseDatabase.
                                getInstance().
                                getReference("ListItems").
                                child(shoppingList.getListUID());

        //2) model
        String productName = etProductName.getText().toString();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            Log.e("Ness", "No user");
            return;
        }

        ShoppingListProduct product = new ShoppingListProduct(productName,
                currentUser.getUid(),
                currentUser.getDisplayName(),
                false
                );


        ref.push().setValue(product);
    }
    public  static  class ProductAdapter
            extends FirebaseRecyclerAdapter<ShoppingListProduct, ProductAdapter.ProductViewHolder>{

        public ProductAdapter( Query query) {
            super(ShoppingListProduct.class, R.layout.product_item, ProductViewHolder.class, query);
        }
        //Binding
        @Override
        protected void populateViewHolder(ProductViewHolder viewHolder, ShoppingListProduct model, int position) {
            viewHolder.tvProduct.setText(model.getProductName());
            viewHolder.model = model;

        }
        //VH
        public static class  ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProduct;
        ShoppingListProduct model;
        Context context;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tvProduct = (TextView) itemView.findViewById(R.id.tvProductName);
            context = itemView.getContext();
        }
    }
    }
}
