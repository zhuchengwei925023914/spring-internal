package testBean;

/**
 * Created by zhu on 2019/3/31.
 */
public abstract class ShowSexClass {
    public void showSex() {
        getPeople().showSex();
    }

    public abstract People getPeople();
}
