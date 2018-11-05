package net.edaibu.easywalking.persenter;

public interface BasePersenter {

    /**
     * 展示加载滚动条
     */
    void showLoding(String msg);


    /**
     * 关闭加载滚动条
     */
    void closeLoding();


    /**
     * 展示Toast数据
     * @param msg
     */
    void showToast(String msg);
}
