package com.lonly.example.fragnmentinterfacedemo.function;

import android.content.Intent;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by lonly on 2017/10/16.
 */

public class FunctionManager {
    private static FunctionManager instance;
    private HashMap<String,FunctionNoParamNoResault> mFunctionNoParamNoResault;
    private HashMap<String,FunctionWithParamOnly> mFunctionWithParamOnly;
    private HashMap<String,FunctionWithResultOnly> mFunctionWithResultOnly;
    private HashMap<String,FunctionWithParamAndResult> mFunctionWithParamAndResult;

    private FunctionManager() {
        mFunctionNoParamNoResault = new HashMap<>();
        mFunctionWithParamOnly = new HashMap<>();
        mFunctionWithResultOnly = new HashMap<>();
        mFunctionWithParamAndResult = new HashMap<>();
    }
    public static FunctionManager getInstance(){
        if(instance == null){
            instance = new FunctionManager();
        }
        return instance;
    }

    /**
     * 添加无参无返回值的接口方法
     * @param function
     * @return
     */

    public FunctionManager addFunction(FunctionNoParamNoResault function){
        mFunctionNoParamNoResault.put(function.mFunctionName,function);
        return this;
    }

    /**
     * 调用无参无返回值的接口方法
     * @param funcName
     */
    public void invokeFunc(String funcName){
        if(TextUtils.isEmpty(funcName) == true){
            return;
        }
        if(mFunctionNoParamNoResault != null){
            FunctionNoParamNoResault f = mFunctionNoParamNoResault.get(funcName);
            if(f != null){
                f.function();
            }
            if (f == null){
                try {
                    throw  new FunctionException("Has no this function" + funcName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 添加无参有返回值的接口方法
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionWithResultOnly function){
        mFunctionWithResultOnly.put(function.mFunctionName,function);
        return this;
    }
    /**
     * 调用无参有返回值的接口方法
     * @param funcName
     */
    public <Result> Result invokeFunc(String funcName,Class<Result> clz){
        if(TextUtils.isEmpty(funcName) == true){
            return null;
        }
        if(mFunctionWithResultOnly != null){
            FunctionWithResultOnly f = mFunctionWithResultOnly.get(funcName);
            if(f != null){
                if(clz != null){
                    return clz.cast(f.function());
                } else {
                    return (Result) f.function();
                }
            }else {
                try {
                    throw  new FunctionException("Has no this function" + funcName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 添加有参有返回值的接口方法
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionWithParamAndResult function){
        mFunctionWithParamAndResult.put(function.mFunctionName,function);
        return this;
    }
    /**
     * 调用有参有返回值的接口方法
     * @param funcName
     */
    public <Result,Param> Result invokeFunc(String funcName,Class<Result> clz ,Param data){
        if(TextUtils.isEmpty(funcName) == true){
            return null;
        }
        if(mFunctionWithParamAndResult != null){
            FunctionWithParamAndResult f = mFunctionWithParamAndResult.get(funcName);
            if(f != null){
                if(clz != null){
                    return clz.cast(f.function(data));
                } else {
                    return (Result) f.function(data);
                }
            }else {
                try {
                    throw  new FunctionException("Has no this function" + funcName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 添加有参无返回值的接口方法
     * @param function
     * @return
     */
    public FunctionManager addFunction(FunctionWithParamOnly function){
        mFunctionWithParamOnly.put(function.mFunctionName,function);
        return this;
    }
    /**
     * 调用有参无返回值的接口方法
     * @param funcName
     */
    public <Param> void invokeFunc(String funcName,Param data ){
        if(TextUtils.isEmpty(funcName) == true){
            return ;
        }
        if(mFunctionWithParamOnly != null){
            FunctionWithParamOnly f = mFunctionWithParamOnly.get(funcName);
            if(f != null){
                f.function(data);
            }else {
                try {
                    throw  new FunctionException("Has no this function" + funcName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
        return ;
    }
}
