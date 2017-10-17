package com.lonly.example.fragnmentinterfacedemo;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.lonly.example.fragnmentinterfacedemo.function.FunctionManager;

/**
 * Created by lonly on 2017/10/16.
 */

public class BaseFragment extends Fragment{

    protected FunctionManager mFunctionManager;
    private MainActivity mBaseActivity;

    /**
     * 为要实现接口的Fragment添加FunctionManager
     * @param functionManager
     */
    public void setmFunctionManager(FunctionManager functionManager){
        this.mFunctionManager = functionManager;
    }

    /**
     * 确保Mainctivity实现了Fragment相应的接口回调
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            mBaseActivity = (MainActivity) context;
            mBaseActivity.setFuctionsForFragment(getTag());
        }
    }
}
