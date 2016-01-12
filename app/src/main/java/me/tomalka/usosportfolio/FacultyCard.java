package me.tomalka.usosportfolio;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class FacultyCard extends CardView {
    private ImageView coverImage;
    private TextView coverTitle;
    private View phoneView;
    private View mapView;
    private TextView phoneText;
    private TextView mapText;
    private CharSequence rawAddress;

    public FacultyCard(Context context) {
        super(context);
        init(context, null, 0);
    }

    public FacultyCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FacultyCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setCoverPhoto(Drawable drawable) {
        coverImage.setImageDrawable(drawable);
    }

    public void setCoverPhoto(RequestCreator picassoCreator) {
        Picasso.with(getContext()).cancelRequest(coverImage);
        picassoCreator.into(coverImage);
    }

    public Drawable getCoverPhoto() {
        return coverImage.getDrawable();
    }

    public void setCoverTitle(CharSequence title) {
        coverTitle.setText(title);
    }

    public CharSequence getCoverTitle() {
        return coverTitle.getText();
    }

    public void setTelephoneNumber(CharSequence telephone) {
        if (telephone != null && telephone.length() != 0)
        {
            phoneText.setText(telephone);
            phoneView.setVisibility(View.VISIBLE);
        } else
            phoneView.setVisibility(View.GONE);
    }

    public CharSequence getTelephoneNumber() {
        return phoneText.getText();
    }

    public void setFacultyAddress(CharSequence address) {
        rawAddress = address;
        if (address != null && address.length() != 0)
        {
            // Forcing map link because not detected otherwise
            SpannableString spanStr = new SpannableString(address);
            spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
            mapText.setText(spanStr);
            mapView.setVisibility(View.VISIBLE);
        } else
            mapView.setVisibility(View.GONE);
    }

    public CharSequence getFacultyAddress(CharSequence address) {
        return rawAddress;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate(context, R.layout.faculty_card, this);

        coverImage = (ImageView)findViewById(R.id.faculty_card_cover);
        coverTitle = (TextView)findViewById(R.id.faculty_card_title);
        phoneView = findViewById(R.id.faculty_card_phone_view);
        phoneText = (TextView)findViewById(R.id.faculty_card_phone_text);
        mapView = findViewById(R.id.faculty_card_map_view);
        mapText = (TextView)findViewById(R.id.faculty_card_map_text);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FacultyCard, defStyleAttr,
                R.style.FacultyCard);

        Drawable coverPhoto = a.getDrawable(R.styleable.FacultyCard_coverPhoto);
        CharSequence title = a.getString(R.styleable.FacultyCard_coverTitle);
        CharSequence phone = a.getString(R.styleable.FacultyCard_telephoneNumber);
        CharSequence address = a.getString(R.styleable.FacultyCard_facultyAddress);

        setCoverPhoto(coverPhoto);
        setCoverTitle(title);
        setTelephoneNumber(phone);
        setFacultyAddress(address);

        // Android studio preview doesn't have lambdas, so we don't use them here
        //noinspection Convert2Lambda
        mapText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + mapText.getText().toString()));
                getContext().startActivity(geoIntent);
            }
        });
        a.recycle();
    }
}
