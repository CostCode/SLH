/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.view.adapter;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import edu.cmu.cc.android.util.WidgetUtils;
import edu.cmu.cc.slh.R;
import edu.cmu.cc.slh.dao.SLItemDAO;
import edu.cmu.cc.slh.model.ItemCategory;
import edu.cmu.cc.slh.model.ShoppingListItem;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  DESCRIPTION: This adapter provides representation for the active 
 *  shopping list. The SL items are displayed within the categories to which
 *  they belong.
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 4, 2013
 */
public class ActiveSLViewListAdapter extends BaseAdapter {


	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	private static final int TYPE_COUNT = 2;
	
	private static final int TYPE_CATEGORY = 0;
	
	private static final int TYPE_ITEM = 1;

	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	private Context ctx;
	
	private List<ItemCategory> categories;
	
	private LayoutInflater inflater;
	
	private IDeleteSLItemCaller deleteCaller;
	

	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	public ActiveSLViewListAdapter(Context ctx, List<ItemCategory> categories, 
			IDeleteSLItemCaller deleteCaller) {
		
		super();
		
		this.ctx = ctx;
		this.categories = categories;
		this.deleteCaller = deleteCaller;
		this.inflater = LayoutInflater.from(ctx);
	}

	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------

	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public int getCount() {
		
		int total = 0;
		
		for (ItemCategory category : categories) {
			if (category != null && category.getItems() != null 
					&& category.getItems().size() > 0) {
				total += category.getItems().size() + 1;
			}
		}
		
		return total;
	}

	@Override
	public Object getItem(int position) {
		
		int curPosition = -1;
		
		for (ItemCategory category : categories) {
			if (category != null && category.getItems() != null 
					&& category.getItems().size() > 0) {
				
				curPosition++;
				if (curPosition == position) {
					return category;
				}
				
				int categoryItemsCount = category.getItems().size();
				
				if (position <= curPosition + categoryItemsCount) {
					return category.getItems().get(position-curPosition-1);
				}
				
				curPosition += categoryItemsCount;
			}
		}
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			
			int type = getItemViewType(position);
			
			if (type == TYPE_CATEGORY) {
				convertView = inflater.inflate(
						R.layout.active_sl_row_category, null);
				viewHolder = getCategoryViewHolder(convertView);
			} else if (type == TYPE_ITEM) {
				convertView = inflater.inflate(
						R.layout.active_sl_row_item, null);
				viewHolder = getItemViewHolder(convertView);
			} else {
				throw new IllegalStateException("Unknown item type");
			}
			
			convertView.setTag(viewHolder);
			
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		customizeView(position, viewHolder);
		
		return convertView;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_CATEGORY);
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		
		Object obj = getItem(position);
		
		if (obj instanceof ItemCategory) {
			return TYPE_CATEGORY;
		}
		
		if (obj instanceof ShoppingListItem) {
			return TYPE_ITEM;
		}
		
		return -1;
	}
	
	//-------------------------------------------------------------------------
	// PRIVATE METHODS
	//-------------------------------------------------------------------------
	
	private ViewHolder getCategoryViewHolder(View view) {
		
		TextView nameView = (TextView) 
				view.findViewById(R.id.tv_active_sl_group_name);
		
		ViewHolder viewHolder = new ViewHolder(null, nameView, null, null);
		
		return viewHolder;
	}
	
	private ViewHolder getItemViewHolder(View view) {
		
		TextView markView = (TextView) 
				view.findViewById(R.id.tv_active_sl_row_item_mark);
		
		TextView nameView = (TextView) 
				view.findViewById(R.id.tv_active_sl_row_item_name);
		
		TextView detailsView = (TextView) 
				view.findViewById(R.id.tv_active_sl_row_item_details);
		
		ImageView deleteView = (ImageView)
				view.findViewById(R.id.btn_active_sl_row_item_delete);
		
		ViewHolder viewHolder = 
				new ViewHolder(markView, nameView, detailsView, deleteView);
		
		return viewHolder;
	} 
	
	private void customizeView(int position, ViewHolder viewHolder) {
		
		Object obj = getItem(position);
		
		if (obj instanceof ItemCategory) {
			customizeCategoryView((ItemCategory)obj, viewHolder);
		} else if (obj instanceof ShoppingListItem) {
			customizeItemView((ShoppingListItem)obj, viewHolder);
		}
	}
	
	private void customizeCategoryView(ItemCategory category, 
			ViewHolder viewHolder) {
		
		viewHolder.getNameView().setText(category.getName());
	}
	
	private void customizeItemView(ShoppingListItem item, 
			ViewHolder viewHolder) {
		
		viewHolder.getMarkView().setText(R.string.sl_item_mark);
		viewHolder.getNameView().setText(item.getName());
		viewHolder.getDetailsView().setText(prepareItemDetails(item));
		
		customizeDeleteImage(viewHolder.getDeleteView(), item);
	}
	
	private String prepareItemDetails(ShoppingListItem item) {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(item.getQuantity());
		builder.append(" ");
		builder.append(ShoppingListItem.Unit.getUnitByCode(item.getUnit()));
		builder.append(" - ");
		builder.append(Currency.getInstance(Locale.US).getSymbol());
		builder.append(String.format("%.2f", item.getPrice().doubleValue()));
		
		return builder.toString();
	}
	
	private void customizeDeleteImage(ImageView deleteImage, 
			final ShoppingListItem item) {
		
		deleteImage.setVisibility(View.VISIBLE);
		deleteImage.setClickable(true);
		deleteImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DialogInterface.OnClickListener deleteListener = 
						new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						new SLItemDAO().delete(item);
						
						Toast.makeText(ctx, 
								R.string.sl_item_delete_success, 
								Toast.LENGTH_LONG).show();
						
						deleteCaller.onSLItemDeleted();
					}
				};
				
				final String deleteMessage = getDeleteMessage(
						R.string.sl_item_delete_message, item);
				
				WidgetUtils.createYesNoAlertDialog(ctx, 
						edu.cmu.cc.android.R.drawable.cancel, 
						R.string.sl_item_delete_title, 
						deleteMessage, deleteListener).show();
			}
			
		});
		
	}
	
	private String getDeleteMessage(int deleteMsgResID, 
			final ShoppingListItem item) {
		return ctx.getString(deleteMsgResID, item.getName());
	}
	
	//-------------------------------------------------------------------------
	// INNER CLASS
	//-------------------------------------------------------------------------
	
	/**
	 *  View Holder design pattern.
	 */
	private static class ViewHolder {
		
		private TextView markView;
		
		private TextView nameView;
		
		private TextView detailsView;
		
		private ImageView deleteView;
		
		public ViewHolder(TextView markView, TextView nameView, 
				TextView detailsView, ImageView deleteView) {
			
			this.markView = markView;
			this.nameView = nameView;
			this.detailsView = detailsView;
			this.deleteView = deleteView;
		}

		public TextView getMarkView() {
			return markView;
		}

		public TextView getNameView() {
			return nameView;
		}

		public TextView getDetailsView() {
			return detailsView;
		}

		public ImageView getDeleteView() {
			return deleteView;
		}
	}
	
	//-------------------------------------------------------------------------
	// INTERFACE
	//-------------------------------------------------------------------------
	
	public interface IDeleteSLItemCaller {
		
		public void onSLItemDeleted();
		
	}

}