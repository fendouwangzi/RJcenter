package com.rsen.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 16-01-18-018.
 */
public abstract class RBaseAdapter<T> extends RecyclerView.Adapter<RBaseViewHolder> {

    protected List<T> mAllDatas;
    protected Context mContext;

    public RBaseAdapter(Context context) {
        mAllDatas = new ArrayList<>();
        this.mContext = context;
    }

    public RBaseAdapter(Context context, List<T> datas) {
        this.mAllDatas = datas == null ? new ArrayList<T>() : datas;
        this.mContext = context;
    }

    //--------------标准的方法-------------//

    @Override
    public RBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemLayoutId = getItemLayoutId(viewType);
        View item;
        if (itemLayoutId == 0) {
            item = createContentView(parent, viewType);
        } else {
            item = LayoutInflater.from(mContext).inflate(itemLayoutId, parent, false);
        }

        return new RBaseViewHolder(item, viewType);
    }

    @Override
    public void onBindViewHolder(RBaseViewHolder holder, int position) {
        onBindView(holder, position, mAllDatas.size() > position ? mAllDatas.get(position) : null);
    }

    @Override
    public int getItemCount() {
        return mAllDatas == null ? 0 : mAllDatas.size();
    }

    //--------------需要实现的方法------------//

    protected View createContentView(ViewGroup parent, int viewType) {
        return null;
    }

    protected abstract int getItemLayoutId(int viewType);

    protected abstract void onBindView(RBaseViewHolder holder, int position, T bean);

    //---------------滚动事件的处理--------------------//

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    }

    //----------------Item 数据的操作-----------------//

    /**
     * 在最后的位置插入数据
     */
    public void addLastItem(T bean) {
        int startPosition = mAllDatas.size();
        mAllDatas.add(bean);
        notifyItemInserted(startPosition);
    }

    /**
     * 解决九宫格添加图片后,添加按钮消失的bug
     */
    public void addLastItemSafe(T bean) {
        int startPosition = mAllDatas.size();
        mAllDatas.add(bean);
        int itemCount = getItemCount();
        if (itemCount > startPosition + 1) {
            notifyItemInserted(startPosition);
        } else {
            notifyDataSetChanged();
        }
    }

    public void addFirstItem(T bean) {
        List<T> tempBeans = new ArrayList<>();
        tempBeans.add(bean);
        tempBeans.addAll(mAllDatas);
        mAllDatas.clear();
        mAllDatas = tempBeans;
        notifyItemInserted(0);
    }

    /**
     * delete item with object
     */
    public void deleteItem(T bean) {
        if (mAllDatas != null) {
            int size = mAllDatas.size();
            int indexOf = mAllDatas.indexOf(bean);
            if (indexOf > -1) {
                if (onDeleteItem(bean)) {
                    mAllDatas.remove(bean);
                    notifyItemRemoved(indexOf);
//                    notifyItemRangeChanged(indexOf, size - indexOf);
                }
            }
        }
    }

    public void deleteItem(int position) {
        if (mAllDatas != null) {
            int size = mAllDatas.size();
            if (size > position) {
                mAllDatas.remove(position);
                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, size - position);
            }
        }
    }

    protected boolean onDeleteItem(T bean) {
        return true;
    }

    public void removeFirstItem() {
        mAllDatas.remove(0);
        notifyItemRemoved(0);
    }

    public void removeLastItem() {
        int last = mAllDatas.size() - 1;
        mAllDatas.remove(last);
        notifyItemRemoved(last);
    }

    /**
     * 重置数据
     */
    public void resetData(List<T> datas) {
        this.mAllDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 追加数据
     */
    public void appendData(List<T> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        if (this.mAllDatas == null) {
            this.mAllDatas = new ArrayList<>();
        }
        int startPosition = this.mAllDatas.size();
        this.mAllDatas.addAll(datas);
        notifyItemRangeInserted(startPosition, datas.size());
    }

    public List<T> getAllDatas() {
        return mAllDatas;
    }
}
