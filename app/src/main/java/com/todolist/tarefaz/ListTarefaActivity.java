package com.todolist.tarefaz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.todolist.tarefaz.adapter.TarefaAdapter;
import com.todolist.tarefaz.dao.TarefaDAO;
import com.todolist.tarefaz.model.Tarefa;
import com.todolist.tarefaz.util.Mensagem;

import java.util.List;


public class ListTarefaActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener {

    private ListView lista;
    private List<Tarefa> tarefaList;
    private TarefaAdapter tarefaAdapter;
    private TarefaDAO tarefaDAO;
    private AlertDialog dialog;
    private AlertDialog confirmacao;
    int idposicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tarefa);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A237E")));

        dialog = Mensagem.CriarAlertDialog(this);
        confirmacao = Mensagem.CriarDialogC0nfirmacao(this);
        tarefaDAO = new TarefaDAO(this);
        tarefaList = tarefaDAO.listarTarefas();
        tarefaAdapter = new TarefaAdapter(this, tarefaList);
        lista = (ListView) findViewById(R.id.lvTarefa);
        lista.setAdapter(tarefaAdapter);
        lista.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_tarefa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.novo_tarefa) {
            startActivity(new Intent(this, CadTarefaActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int id = tarefaList.get(idposicao).get_id();
        switch (which) {
            case 0:
                Intent intent = new Intent(this, CadTarefaActivity.class);
                intent.putExtra("TAREFA_ID", id);
                startActivity(intent);
            case 1:
                confirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                tarefaList.remove(idposicao);
                tarefaDAO.removerTarefas(id);
                lista.invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                confirmacao.dismiss();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        idposicao = position;
        dialog.show();
    }
}

