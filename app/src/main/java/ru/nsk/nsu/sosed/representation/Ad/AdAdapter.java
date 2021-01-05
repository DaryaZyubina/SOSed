package ru.nsk.nsu.sosed.representation.Ad;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosed.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.nsk.nsu.sosed.domain.Ad;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {

private final List<Ad> list;

public AdAdapter(List<Ad> list) {
        this.list = list;
        }

@Override
public AdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
        return new AdViewHolder(view);
        }

@Override
public void onBindViewHolder(AdViewHolder holder, int position) {
    holder.onBind(list.get(position));
}

@Override
public int getItemCount() {
        return list.size();
        }

public static class AdViewHolder extends RecyclerView.ViewHolder {

    private Ad item;

    TextView title;
    TextView text;
    TextView photoAttached;
    TextView createdDate;

    public AdViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title_text_view);
        text = itemView.findViewById(R.id.text_text_view);
        photoAttached = itemView.findViewById(R.id.photo_attached_text_view);
        createdDate = itemView.findViewById(R.id.created_date_text_view);
    }

    void onBind(final Ad item) {
        this.item = item;

        title.setText(item.getTitle());
        text.setText(item.getText());
        if(item.getImageUrl()!=null){
            photoAttached.setText("(Фото)"); //todo: перенести в строки-константы
        }
        else{
            photoAttached.setText("");
        }

        final String dateString = getDateString(item.getCreatedDate());
        createdDate.setText(dateString);
    }

    private View.OnClickListener onItemClickListener() {
        return view -> {
            if (null == item) {
                Log.e("ad adapter:view holder", "Ad item is null in ViewHolder.");
                return;
            }
            //goToTheDetailsScreen(item); // этот метод у активити мб??
        };
    }

    private void goToTheDetailsScreen(Ad item) {
        //final Intent intent = AdDetailsfragment.getIntent(this, item);
        //activity.startActivity(intent);
    }

    private String getDateString(final Date date) {
        return SimpleDateFormat.getInstance().format(date);
    }
}
}