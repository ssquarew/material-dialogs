package com.initialz.materialdialogs.simplelist;

import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.initialz.materialdialogs.MaterialDialog;
import com.initialz.materialdialogs.R;
import com.initialz.materialdialogs.internal.MDAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * See the sample project to understand how this is used. Mimics the Simple List dialog style
 * displayed on Google's guidelines site:
 * https://www.google.com/design/spec/components/dialogs.html#dialogs-simple-dialogs
 *
 * @author Aidan Follestad (afollestad)
 */
public class MaterialSimpleListAdapter
    extends RecyclerView.Adapter<MaterialSimpleListAdapter.SimpleListVH> implements MDAdapter {

  private MaterialDialog dialog;
  private List<MaterialSimpleListItem> items;
  private Callback callback;

  public MaterialSimpleListAdapter(Callback callback) {
    items = new ArrayList<>(4);
    this.callback = callback;
  }

  public void add(MaterialSimpleListItem item) {
    items.add(item);
    notifyItemInserted(items.size() - 1);
  }

  public void clear() {
    items.clear();
    notifyDataSetChanged();
  }

  public MaterialSimpleListItem getItem(int index) {
    return items.get(index);
  }

  @Override
  public void setDialog(MaterialDialog dialog) {
    this.dialog = dialog;
  }

  @Override
  public SimpleListVH onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.md_simplelist_item, parent, false);
    return new SimpleListVH(view, this);
  }

  @Override
  public void onBindViewHolder(SimpleListVH holder, int position) {
    if (dialog != null) {
      final MaterialSimpleListItem item = items.get(position);
      if (item.getIcon() != null) {
        holder.icon.setImageDrawable(item.getIcon());
        holder.icon.setPadding(
            item.getIconPadding(),
            item.getIconPadding(),
            item.getIconPadding(),
            item.getIconPadding());
      } else {
        holder.icon.setVisibility(View.GONE);
      }

      if (TextUtils.isEmpty(item.getDescription())){
        holder.description.setVisibility(View.GONE);
      }else {
        holder.description.setVisibility(View.VISIBLE);
      }

      holder.title.setText(item.getContent());
      holder.description.setText(item.getDescription());
      holder.infoRight.setText(item.getInfoRight());

      holder.infoCheck.setVisibility(item.isInfoCheck()?View.VISIBLE:View.GONE);

      if (item.isInfoCheck() || !TextUtils.isEmpty(item.getInfoRight())){
        ((LinearLayout.LayoutParams)holder.linearLayoutTitle.getLayoutParams()).rightMargin = holder.linearLayoutTitle.getContext().getResources().getDimensionPixelSize(R.dimen.md_simpleitem_title_right_margin);
      }else {
        ((LinearLayout.LayoutParams)holder.linearLayoutTitle.getLayoutParams()).rightMargin = 0;

      }

      dialog.setTypeface(holder.title, dialog.getBuilder().getRegularFont());
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public interface Callback {

    void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item);
  }

  static class SimpleListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    final ImageView icon;
    final TextView title;
    final TextView description;
    final TextView infoRight;
    final ImageView infoCheck;
    final MaterialSimpleListAdapter adapter;
    final LinearLayout linearLayoutTitle;

    SimpleListVH(View itemView, MaterialSimpleListAdapter adapter) {
      super(itemView);
      icon = (ImageView) itemView.findViewById(android.R.id.icon);
      title = (TextView) itemView.findViewById(android.R.id.title);
      description = (TextView) itemView.findViewById(android.R.id.message);
      infoRight = (TextView) itemView.findViewById(R.id.info_right);
      infoCheck = (ImageView) itemView.findViewById(R.id.info_check);
      linearLayoutTitle = (LinearLayout)itemView.findViewById(R.id.linearLayoutTitle);

      this.adapter = adapter;
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      if (adapter.callback != null) {
        adapter.callback.onMaterialListItemSelected(
            adapter.dialog, getAdapterPosition(), adapter.getItem(getAdapterPosition()));
      }
    }
  }
}
