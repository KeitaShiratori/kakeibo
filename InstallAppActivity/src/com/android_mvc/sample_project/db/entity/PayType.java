package com.android_mvc.sample_project.db.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android_mvc.sample_project.db.dao.PayTypeDAO;
import com.android_mvc.sample_project.db.entity.lib.LogicalEntity;
import com.android_mvc.sample_project.db.schema.ColumnDefinition.PayTypeCol;

public class PayType extends LogicalEntity<PayType> {
    // Intent経由でエンティティを運搬可能にするために
    private static final long serialVersionUID = 1L;

    @Override
    public String tableName(){return "pay_type";}

    @Override
    public final String[] columns(){
        return new String[]{ PayTypeCol.ID,
        		PayTypeCol.PAY_TYPE_NAME,
        	};
    }


	// メンバ
    private String pay_type_name = null;


    // IDEが自動生成したG&S

	public String getPayTypeName() {
		return pay_type_name;
	}

	public void setPayTypeName(String pay_type_name) {
		this.pay_type_name = pay_type_name;
	}



    // カスタムG&S
	
	public Spinner getSpinner(Context context){
		Spinner ret = new Spinner(context);
		List<PayType> payList = new PayTypeDAO(context).findAll();
		List<CharSequence> list = new ArrayList<CharSequence>();
		for (PayType p : payList){
			list.add(p.getPayTypeName());
		}
		ArrayAdapter<CharSequence> PayAdapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, list);

		ret.setAdapter(PayAdapter);
		return ret;
	}

    // ----- LP変換(Logical <-> Physical) -----


    /**
     * DBの格納値から論理エンティティを構成
     */
    @Override
    public PayType logicalFromPhysical(Cursor c)
    {
        setId(c.getLong(0));
        setPayTypeName(c.getString(1));

        return this;
    }


    /**
     * 自身をDBに新規登録可能なデータ型に変換して返す
     */
    @Override
    protected ContentValues toPhysicalEntity(ContentValues values)
    {
        // entityをContentValueに変換

        if( getId() != null)
        {
            values.put(PayTypeCol.ID, getId());
        }

        if( getPayTypeName() != null){
        	values.put(PayTypeCol.PAY_TYPE_NAME, getPayTypeName());
        }
        
        return values;
    }


}
