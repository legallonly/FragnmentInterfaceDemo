# FragnmentInterfaceDemo
Fragment之间通讯的万能接口框架
实现两个Fragment之间的通讯方法有很多种，如：
EventBus：使用方便，但其使用的是反射原理，会有稍微的延迟，并且他人维护不方便；
static静态变量：使用方便，但是，每个static变量都会占用一块内存区，Android系统分配给每个App的内存是有限的（63M），过多很容易造成App内存溢出；
广播Broadcast Receiver：Android的广播是有限制的，除了系统的广播外，其他的广播尽量少用。另外，广播会有延迟；
接口：接口是我们常用的Fragment之间的通讯方式，通过一个主Activity作为通讯桥梁（谷歌官方声明：两个Fragment之间永远不要直接通讯），实现两个Fragment之间的通讯。
接口的方式是我们推荐的，但是，传统的接口方式会造成一些问题，如果主Activity实现了多个Fragment的通讯回调接口，它的代码结构将是这样的：
第一步：定义接口
[java] view plain copy
public interface Fragment1CallBack {  
    void buttonClick1();  
}  
第二步：Fragment调用接口
[java] view plain copy
public class Fragment1 extends Fragment {  
  
    private TextView tv;  
    private Button btn;  
    private Fragment1CallBack fragment1CallBack;  
  
    @Nullable  
    @Override  
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.fragment_layout,null);  
        tv = (TextView) view.findViewById(R.id.textView);  
        btn = (Button) view.findViewById(R.id.button);  
        btn.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                //调用接口方法  
                fragment1CallBack.buttonClick1();  
            }  
        });  
        tv.setText(this.getClass().getSimpleName());  
        return view;  
    }  
  
    @Override  
    public void onAttach(Context context) {  
        super.onAttach(context);  
        // 保证容器Activity实现了回调接口 否则抛出异常警告  
        try {  
            fragment1CallBack = (Fragment1CallBack) context;  
        } catch (ClassCastException e) {  
            throw new ClassCastException(context.toString()  
                    + " must implement Fragment1CallBack");  
        }  
    }  
}  
第三步：Activity实现接口回调
[java] view plain copy
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,  
                                                                Fragment1CallBack,  
                                                                Fragment2CallBack,  
                                                                Fragment3CallBack,  
                                                                Fragment4CallBack{  
  
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
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);  
        viewpager.setAdapter(adapter);  
        viewpager.setCurrentItem(0,false);  
        viewpager.setOffscreenPageLimit(3);  
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
        }  
    }  
    @Override  
    public void buttonClick1() {  
        // Fragment1回调  
  
    }  
    @Override  
    public void buttonClick2() {  
        // Fragment2回调  
  
    }  
    @Override  
    public void buttonClick3() {  
        // Fragment3回调  
  
    }  
    @Override  
    public void buttonClick4() {  
        // Fragment4回调  
  
    }  
}  

上述传统方式，在回调接口比较少的情况下还可以接受，但是，若接口很多，那么，我们需要在Activity类声明中implements很多的接口，类中还要实现一大堆接口方法，显得特别繁琐。
为了解决这种过于繁琐的操作，我们采用面向对象思想，将接口抽象成一个对象，一个接口中包含有函数（函数名、返回值、参数、没有实现的函数体）。因此，我们可以定义出下面几个类：
Function类（接口抽象类的基础类）：
[java] view plain copy
public abstract class Function {  
     public String mFunctionName;  
  
    public Function(String functionName){  
        this.mFunctionName = functionName;  
    }  
  
}  
FunctionNoParamNoResault类（无参无返回值接口抽象类）：
[java] view plain copy
public abstract class FunctionNoParamNoResault extends Function{  
    public FunctionNoParamNoResault(String functionName){  
        super(functionName);  
    }  
    public abstract void function();  
}  
FunctionWithParamAndResult类（有参有返回值接口抽象类）：
[java] view plain copy
public abstract class FunctionWithParamAndResult<Result,Param> extends Function{  
    public FunctionWithParamAndResult(String functionName){  
        super(functionName);  
    }  
    public abstract Result function(Param pram);  
}  
FunctionWithParamOnly类（有参无返回值接口抽象类）：
[java] view plain copy
public abstract class FunctionWithParamOnly<Param> extends Function{  
    public FunctionWithParamOnly(String functionName){  
        super(functionName);  
    }  
    public abstract void function(Param pram);  
}  

FunctionWithResultOnly类（无参有返回值接口抽象类）：
[java] view plain copy
public abstract class FunctionWithResultOnly<Result> extends Function{  
    public FunctionWithResultOnly(String functionName){  
        super(functionName);  
    }  
    public abstract Result function();  
}  

定义完所有的接口抽象类之后，我们在定义一个接口管理类，通过接口管理类来管理调用对应的函数方法。
FunctionManager类（管理和调用不同的方法）：
[java] view plain copy
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

然后，定义一个要使用回调接口的Fragment的基类。
BaseFragment类：
[java] view plain copy
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

在具体某个Fragment类中，调用接口。如：
Fragment3类（调用有参有返回值接口方法）：
[java] view plain copy
public class Fragment3 extends BaseFragment{  
    private TextView tv;  
    private Button btn;  
    //定义一个接口标记  
    public static final String INTERFACE_PARAM_RESULT = Fragment1.class.getName() + "PR";  
  
    @Nullable  
    @Override  
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.fragment_layout,null);  
        tv = (TextView) view.findViewById(R.id.textView);  
        tv.setText(this.getClass().getSimpleName());btn = (Button) view.findViewById(R.id.button);  
        btn.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                //这里调用接口  
                String str = mFunctionManager.invokeFunc(INTERFACE_PARAM_RESULT,String.class,"我是传输的字符串");  
                Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();  
            }  
        });  
  
        return view;  
    }  
}  

MainActivity中添加接口并实现接口方法：
MainActivity类：
[java] view plain copy
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



至此，一个实现Fragment之间数据通讯的万能接口框架就搭建完成了。
