package com.example.myproject.AdditionalEvent.Navigation;

import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myproject.MainActivities.FragmentSceneManager;
import com.example.myproject.MainActivities.MainActivity;
import com.example.myproject.R;

public class ParentNavigationActivity extends AppCompatActivity  implements FragmentSceneManager.UpdateNavigationParams {
    private NavigationLayout _navigationLayout;
    private ConstraintLayout _left_drawer;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setupMenu();
    }

    public void setupMenu()
    {
        _left_drawer =(ConstraintLayout) findViewById(R.id.left_drawer);
        _navigationLayout =new NavigationLayout(getApplicationContext(), _left_drawer);

        InitParams();

        _left_drawer.addView(_navigationLayout);
    }

    private void InitParams(){
        SetMoneySideViewText(MainActivity.character.getMoney());
        SetPopularitySideViewText(MainActivity.character.getPopularity());
        SetFatherRelSideViewText(MainActivity.character.getFatherRel());
        SetFightingSkillSideViewText(MainActivity.character.getFightSkill());
        SetShopLVLSideViewText(MainActivity.character.getShopLvl());
        SetArmorSideViewText(MainActivity.character.isHasEquip()? "Доспех есть": "Доспеха нет");
        SetHorseSideViewText(MainActivity.character.isHasHorse()? "Лошадь есть": "Лошади нет");
    }

    protected void SetMoneySideViewText(int value){
        TextView mText = findViewById(R.id.MoneySideViewText);
        mText.setText(Integer.toString(value));
    }

    protected void SetPopularitySideViewText(int value){
        TextView mText = findViewById(R.id.PopularitySideViewText);
        mText.setText(Integer.toString(value));
    }

    protected void SetFatherRelSideViewText(int value){
        TextView mText = findViewById(R.id.FatherRelSideViewText);
        mText.setText(Integer.toString(value));
    }

    protected void SetFightingSkillSideViewText(int value){
        TextView mText = findViewById(R.id.FightingSkillSideViewText);
        mText.setText(Integer.toString(value));
    }

    protected void SetShopLVLSideViewText(int value){
        TextView mText = findViewById(R.id.ShopLVLSideViewText);
        mText.setText(Integer.toString(value));
    }

    protected void SetArmorSideViewText(String value){
        TextView mText = findViewById(R.id.ArmorSideViewText);
        mText.setText(value);
    }

    protected void SetHorseSideViewText(String value){
        TextView mText = findViewById(R.id.HorseSideViewText);
        mText.setText(value);
    }

    @Override
    public void UpdateParam(String name, int value) {
        switch (name){
            case "Money":{
                SetMoneySideViewText(value);
                break;
            }
            case "Popularity":{
                SetPopularitySideViewText(value);
                break;
            }
            case "FatherRelations":{
                SetFatherRelSideViewText(value);
                break;
            }
            case "FightingSkill":{
                SetFightingSkillSideViewText(value);
                break;
            }
            case "ShopLvl":{
                SetShopLVLSideViewText(value);
                break;
            }
        }
    }

    @Override
    public void UpdateParam(String name, boolean value) {
        switch (name){
            case "HasEquip":{
                SetArmorSideViewText(value?"Есть доспех":"Доспеха нет");
                break;
            }
            case "HasHorse":{
                SetHorseSideViewText(value?"Есть лошадь":"Лошади нет");
                break;
            }
        }
    }
}