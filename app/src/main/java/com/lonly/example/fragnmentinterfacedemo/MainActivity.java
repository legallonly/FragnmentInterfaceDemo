package com.lonly.example.fragnmentinterfacedemo;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lonly.example.fragnmentinterfacedemo.adapter.MyPagerAdapter;
import com.lonly.example.fragnmentinterfacedemo.function.FunctionManager;
import com.lonly.example.fragnmentinterfacedemo.function.FunctionNoParamNoResault;
import com.lonly.example.fragnmentinterfacedemo.function.FunctionWithParamAndResult;
import com.lonly.example.fragnmentinterfacedemo.function.FunctionWithParamOnly;
import com.lonly.example.fragnmentinterfacedemo.function.FunctionWithResultOnly;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private ViewPager viewpager;
    private RadioGroup radioGroup;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());
        fragments.add(new Fragment4());
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0,false);
        viewpager.setOffscreenPageLimit(4);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.radio1:
                viewpager.setCurrentItem(0);
                break;
            case R.id.radio2:
                viewpager.setCurrentItem(1);
                break;
            case R.id.radio3:
                viewpager.setCurrentItem(2);
                break;
            case R.id.radio4:
                viewpager.setCurrentItem(3);
                break;
        }
    }

    /**
     * 添加接口并实现接口中的方法回调
     * @param tag Fragment标记
     */
    public void setFuctionsForFragment(String tag){
        FragmentManager fm = getSupportFragmentManager();
        BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(tag);
        FunctionManager functionManager = FunctionManager.getInstance();
        fragment.setmFunctionManager(functionManager.addFunction(new FunctionNoParamNoResault(Fragment1.INTERFACE) {
            @Override
            public void function() {
                //接口中的方法回调
                Toast.makeText(MainActivity.this,"成功调用无参无返回值的接口",Toast.LENGTH_SHORT).show();
            }
        }).addFunction(new FunctionWithResultOnly<String>(Fragment2.INTERFACE_RESULT) {
            @Override
            public String function() {
                //接口中的方法回调
                return "I Love U";
            }
        }).addFunction(new FunctionWithParamAndResult<String,String>(Fragment3.INTERFACE_PARAM_RESULT) {
            @Override
            public String function(String pram) {
                //接口中的方法回调
                return pram;
            }
        }).addFunction(new FunctionWithParamOnly<String>(Fragment4.INTERFACE_PARAM) {

            @Override
            public void function(String pram) {
                //接口中的方法回调
                Toast.makeText(MainActivity.this,"成功调用有参无返回值的接口：" + pram,Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
