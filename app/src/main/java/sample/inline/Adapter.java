package sample.inline;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vdopia.ads.lw.LVDOBannerAd;

import java.util.ArrayList;

import sample.inline.databinding.AdItemBinding;
import sample.inline.databinding.ItemBinding;
import sample.inline.model.Item;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   private static final String TAG = "KevinAdapter";
   private static final int TYPE_NORMAL = 0;
   private static final int TYPE_ADS = 1;

   private ArrayList<ItemWrapper> itemWrappers = new ArrayList<>();

   public void setItemWrappers(ArrayList<ItemWrapper> itemWrappers) {
      this.itemWrappers = itemWrappers;
      notifyItemRangeInserted(0, itemWrappers.size());
   }

   public ArrayList<ItemWrapper> getItemWrappers() {
      return this.itemWrappers;
   }

   public void insertAd(int position, View adView, LVDOBannerAd bannerAd) {
      removeAd();
      ItemWrapper itemWrapper = new ItemWrapper(adView, bannerAd);
      itemWrappers.add(position, itemWrapper);
      notifyItemInserted(position);
   }

   public void removeAd() {
      int adPos = getAdPosition();
      if (adPos != -1) {
         ItemWrapper removed = itemWrappers.remove(adPos);
         notifyItemRemoved(adPos);
         removed.bannerAd.destroyView();
         ((ViewGroup) removed.adView.getParent()).removeAllViews();
         Log.d(TAG, "ad removed");
      }
   }

   public int getAdPosition() {
      for (int i = 0; i < itemWrappers.size(); i++) {
         ItemWrapper itemWrapper = itemWrappers.get(i);
         if (itemWrapper.type == TYPE_ADS) {
            return i;
         }
      }
      return -1;
   }

   @Override
   public void onViewRecycled(RecyclerView.ViewHolder holder) {

      if (holder instanceof AdHolder) {
         //AdHolder adHolder = (AdHolder) holder;
         Log.d(TAG, "onViewRecycled() adHolder");
      }
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if (viewType == TYPE_NORMAL) {
         ItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item, parent, false);
         return new ItemHolder(binding);
      } else {
         AdItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ad_item, parent, false);
         return new AdHolder(binding);
      }

   }

   @Override
   public int getItemViewType(int position) {
      return itemWrappers.get(position).type;
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      ItemWrapper itemWrapper = itemWrappers.get(position);
      if (itemWrapper.type == TYPE_NORMAL) {
         ((ItemHolder) holder).binding.setItem(itemWrapper.item);
      } else {
         AdHolder adHolder = (AdHolder) holder;
         if (adHolder.binding.adContainer.getChildCount() == 0) {
            ((ViewGroup) itemWrapper.adView.getParent()).removeView(itemWrapper.adView);
            adHolder.binding.adContainer.addView(itemWrapper.adView);
            Log.d(TAG, "ad bind to view (adView added to container)");
         } else {
            Log.d(TAG, "ad bind to view (NO view added to container)");
         }
      }
   }

   @Override
   public int getItemCount() {
      return itemWrappers.size();
   }

   static class ItemHolder extends RecyclerView.ViewHolder {
      private ItemBinding binding;

      public ItemHolder(ItemBinding binding) {
         super(binding.getRoot());
         this.binding = binding;
      }
   }

   static class AdHolder extends RecyclerView.ViewHolder {
      private AdItemBinding binding;

      public AdHolder(AdItemBinding binding) {
         super(binding.getRoot());
         this.binding = binding;
      }
   }

   static class ItemWrapper {
      ItemWrapper(Item item) {
         this.type = TYPE_NORMAL;
         this.item = item;
      }

      ItemWrapper(View adView, LVDOBannerAd bannerAd) {
         type = TYPE_ADS;
         this.adView = adView;
         this.bannerAd = bannerAd;
      }

      int type;
      Item item;
      View adView;
      LVDOBannerAd bannerAd;
   }
}
