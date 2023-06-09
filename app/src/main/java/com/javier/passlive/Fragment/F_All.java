package com.javier.passlive.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.javier.passlive.Adapter.Adapter_bank;
//import com.javier.passlive.Adapter.Adapter_card;
import com.javier.passlive.Adapter.Adapter_bank;
import com.javier.passlive.Adapter.Adapter_card;
import com.javier.passlive.Adapter.Adapter_web;
import com.javier.passlive.BBDD.BBDD_Helper;
//import com.javier.passlive.DAO.BankDAO;
import com.javier.passlive.BBDD.Constans;
import com.javier.passlive.Category.Category;
import com.javier.passlive.R;


public class F_All extends Fragment {
    String category;
    String newOrder= Constans.W_RECORD_TIME +  " DESC";
    String sortPast= Constans.W_RECORD_TIME + " ASC";
    String orderTittleAsc = Constans.W_TITTLE + " ASC";
    String orderTittleDesc = Constans.W_TITTLE + " DESC";
    String statusOrder = orderTittleAsc;
   Dialog dialog, dialog_order, dialog_category;
 BBDD_Helper helper;
    RecyclerView RView_record;
    FloatingActionButton btn_add_record;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //No permite captura de pantalla
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        RView_record = view.findViewById(R.id.RView_record);
        btn_add_record= view.findViewById(R.id.btn_add_record);
        helper = new BBDD_Helper(getActivity());
        dialog = new Dialog(getActivity());
        dialog_order = new Dialog(getActivity());



//Listar registros
        loadRecord(orderTittleAsc);
                btn_add_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(getActivity(), Category.class);
                        startActivity(intent);
                    }
                });
        return view;
    }
//Método para cargar registros
    private void loadRecord(String orderby) {
            statusOrder = orderby;
            Adapter_web adapter_web = new Adapter_web(getActivity(), helper.GetAllrecordWeb(orderby));
            Adapter_bank adapter_bank = new Adapter_bank(getActivity(), helper.GetAllrecordBank(orderby));
            Adapter_card adapter_card = new Adapter_card(getActivity(), helper.GetAllrecordCard(orderby));

        ConcatAdapter concatAdapter = new ConcatAdapter(adapter_web, adapter_bank, adapter_card);
            RView_record.setAdapter(concatAdapter);

    }

    //Buscar registro en base de datos
        private void Record_seach(String consultation){
            Adapter_web adapter_web = new Adapter_web(getActivity(), helper.search_RecordsWeb(consultation));
            Adapter_bank adapter_bank = new Adapter_bank(getActivity(), helper.search_RecordsBank(consultation));
            Adapter_card adapter_card = new Adapter_card(getActivity(), helper.search_RecordsCard(consultation));
            ConcatAdapter concatAdapter = new ConcatAdapter(adapter_web, adapter_bank, adapter_card);

        RView_record.setAdapter(concatAdapter);
 }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_all, menu);

        MenuItem item = menu.findItem(R.id.menu_Record_seach);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Método que se ejecuta cuando los usuarios presionan el botón de búsqueda del teclado
            @Override
            public boolean onQueryTextSubmit(String query) {
                Record_seach(query);
                return true;
            }
            //Método que permite realizar la búsqueda mientras el usuario está escribiendo en el teclado
            @Override
            public boolean onQueryTextChange(String newText) {
                Record_seach(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_Order_record){
            Order_Record();
            return true;
        }
        if (id == R.id.menu_Record_number){
            Display_total_records();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
//Para refrescar la lista cuando estemos en el fragmento
    @Override
    public void onResume() {
        loadRecord(statusOrder);
        super.onResume();
    }

    //Método para visualizar total de registros
    public void Display_total_records(){
        TextView Total;
        Button BtnFully_Undertood;

        dialog.setContentView(R.layout.box_total_records_dialog);
        Total = dialog.findViewById(R.id.Total);
        BtnFully_Undertood = dialog.findViewById(R.id.BtnFully_Undertood);

        //Obtenemos valor entero de registro

        int total = helper.GetRecordNumber();

        //Convertir a String el número total de registros

        String total_String = String.valueOf(total);
        Total.setText(total_String);

        BtnFully_Undertood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cuando presionamos boton se cierra el cuadro de dialogo
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }

    private void Order_Record(){
        Button Btn_Category, Btn_New, Btn_Past, Btn_Asc, Btn_Desc;
        //Conexión hacia el diseño
        dialog_order.setContentView(R.layout.box_dialog_order_record);
        //dialog_category.setContentView(R.layout.box_order_category);

        Btn_Category = dialog_order.findViewById(R.id.Btn_Category);
        Btn_New = dialog_order.findViewById(R.id.Btn_New);
        Btn_Past = dialog_order.findViewById(R.id.Btn_Past);
        Btn_Asc = dialog_order.findViewById(R.id.Btn_Asc);
        Btn_Desc = dialog_order.findViewById(R.id.Btn_Desc);

        Btn_Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_order.dismiss();
                orderRecordsByCategory();

            }
        });
        Btn_New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecord(newOrder);
                dialog_order.dismiss();
            }
        });

        Btn_Past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecord(sortPast);
                dialog_order.dismiss();
            }
        });
        Btn_Asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecord(orderTittleAsc);
                dialog_order.dismiss();
            }
        });
        Btn_Desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loadRecord(orderTittleDesc);
                dialog_order.dismiss();
            }
        });
        dialog_order.show();
        dialog_order.setCancelable(true);

    }

    private void orderRecordsByCategory() {
        Button Btn_Order_Web, Btn_Order_Bank, Btn_Order_Card;
        dialog_order.setContentView(R.layout.box_order_category);

        Btn_Order_Web = dialog_order.findViewById(R.id.Btn_Order_Web);
        Btn_Order_Bank = dialog_order.findViewById(R.id.Btn_Order_Bank);
        Btn_Order_Card = dialog_order.findViewById(R.id.Btn_Order_Card);
        Btn_Order_Web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adapter_web adapter_web = new Adapter_web(getActivity(), helper.GetAllrecordWeb(statusOrder));
                RView_record.setAdapter(adapter_web);
                dialog_order.dismiss();
            }
        });

        Btn_Order_Bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adapter_bank adapter_bank = new Adapter_bank(getActivity(), helper.GetAllrecordBank(statusOrder));
                RView_record.setAdapter(adapter_bank);
                dialog_order.dismiss();

            }
        });

       Btn_Order_Card.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Adapter_card adapter_card = new Adapter_card(getActivity(), helper.GetAllrecordCard(statusOrder));
               RView_record.setAdapter(adapter_card);
               dialog_order.dismiss();
           }
       });
        dialog_order.show();
        dialog_order.setCancelable(true);
    }


}