package net.edaibu.easywalking.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.bumptech.glide.Glide;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.activity.diy.DiyActivity;
import net.edaibu.easywalking.bean.DiyBean;
import net.edaibu.easywalking.view.CircleImageView;
import java.util.List;

public class DiyAdapter extends BaseAdapter {

	private Context context;
	private List<DiyBean.templateListBean> list;
	public DiyAdapter(Context context, List<DiyBean.templateListBean> list) {
		super();
		this.context = context;
		this.list=list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if(view==null){
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.diy_item, null);
			holder.img=(CircleImageView)view.findViewById(R.id.img_di);
			view.setTag(holder);
		}else{
			holder=(ViewHolder)view.getTag();
		}
		final DiyBean.templateListBean templateListBean=list.get(position);
		if(templateListBean!=null){
			Glide.with(context).load(templateListBean.getImgUrl()).error(R.mipmap.xl_loding_fault).into(holder.img);
			holder.img.setId(position);
			holder.img.setOnLongClickListener(new View.OnLongClickListener() {
				public boolean onLongClick(View v) {
					final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(new long[]{0,100}, -1);
					final int index=v.getId();
					((DiyActivity)context).setDazzle((CircleImageView)v,list.get(index).getId());
					return true;
				}
			});
		}

		return view;
	}

	private class ViewHolder{
		private CircleImageView img;
	}
}
