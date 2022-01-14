package com.thomaskorsten.refereeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CalculatorActivity extends AppCompatActivity {

    class Referee {
        public int distance, fare, regionalMoney;
        public String name;
        public Position position;

        private Referee() {
            this(Position.OTHER);
        }

        Referee(Position position) {
            this.position = position;
            resetName();
            distance = 0;
            fare = 0;
            regionalMoney = 0;
        }

        public void resetName() {
            switch (position) {
                case R:
                    this.name = r;
                    break;
                case CJ:
                    this.name = cj;
                    break;
                case U:
                    this.name = u;
                    break;
                case LM:
                    this.name = lm;
                    break;
                case LJ:
                    this.name = lj;
                    break;
                case SJ:
                    this.name = sj;
                    break;
                case FJ:
                    this.name = fj;
                    break;
                case BJ:
                    this.name = bj;
                    break;
                default:
                    this.name = other;
            }
        }
    }

    boolean tournament, sameRegion;
    int numOfRefs, sum, page, spinnerSelection, everyoneGets, remainingMoney, screenProgress, calculationID;  // TODO: add calculation ID to save previous calculations permanently
    String r, cj, u, lm, lj, sj, fj, bj, other;                 // TODO: add toggle for "Regions 5 Euro"
    ArrayList<Referee> refList;
    TextView tv_numOfRefs, tv_sum, tv_info, tv_remainder;
    CheckBox cb_sameRegion;
    LinearLayout containerLayout;
    ArrayAdapter<CharSequence> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: load primary color only once and replace getResources() calls

        r = getResources().getString(R.string.R);
        u = getResources().getString(R.string.U);
        lm = getResources().getString(R.string.LM);
        lj = getResources().getString(R.string.LJ);
        sj = getResources().getString(R.string.SJ);
        fj = getResources().getString(R.string.FJ);
        bj = getResources().getString(R.string.BJ);
        cj = getResources().getString(R.string.CJ);
        other = getResources().getString(R.string.OTHER);

        page = 0;
        numOfRefs = 7;
        screenProgress = 0;
        tournament = false;
        sameRegion = false;
        spinnerSelection = 2;
        refList = new ArrayList<>();

        updateScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    void updateScreen() {
        switch(page) {
            case 0:     // select league, referees, fare
                setContentView(R.layout.activity_calculator_league);

                tv_numOfRefs = findViewById(R.id.calculator_value_numofrefs);
                tv_sum = findViewById(R.id.calculator_value_payment);
                tv_info = findViewById(R.id.calculator_value_info);
                Spinner spinner = findViewById(R.id.spinner);
                cb_sameRegion = findViewById(R.id.calculator_value_sameRegion);
                cb_sameRegion.setChecked(sameRegion);

                spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.calculator_arr_leagues, android.R.layout.simple_spinner_item);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnerSelection = position;
                        screenProgress = 0;
                        switch(position) {
                            case 0:     // GFL
                                updateNumberOfRefs(7);
                                tv_info.setText(R.string.calculator_txt_payinfo1);
                                tournament = false;
                                cb_sameRegion.setChecked(false);
                                cb_sameRegion.setEnabled(false);
                                sameRegion = false;
                                break;
                            case 1:     // GFL-2, DBL
                                updateNumberOfRefs(7);
                                tv_info.setText(R.string.calculator_txt_payinfo2);
                                tournament = false;
                                cb_sameRegion.setEnabled(true);
                                break;
                            case 2:     // andere Seniorenligen, DBL-2, DFFL, GFL-J
                                updateNumberOfRefs(7);
                                tv_info.setText(R.string.calculator_txt_payinfo3);
                                tournament = false;
                                cb_sameRegion.setEnabled(true);
                                break;
                            case 3:     // 11er-Tackle Jugendligen (NRW)
                                sum = 280;
                                updateNumberOfRefs(7);
                                tv_info.setText(R.string.calculator_txt_payinfo4);
                                tournament = false;
                                cb_sameRegion.setEnabled(true);
                                break;
                            case 4:     // 9er-Tackle Jugendligen (NRW)
                                sum = 240;
                                updateNumberOfRefs(5);
                                tv_info.setText(R.string.calculator_txt_payinfo5);
                                tournament = false;
                                cb_sameRegion.setEnabled(true);
                                break;
                            case 5:     // 5er-Tackle Jugendligen und Damen (4er Turnier)
                                sum = 280;
                                updateNumberOfRefs(6);
                                tv_info.setText(R.string.calculator_txt_payinfo6);
                                tournament = true;
                                cb_sameRegion.setEnabled(true);
                                break;
                            case 6:     // 5er-Tackle Jugendligen und Damen (3er Turnier)
                                sum = 180;
                                updateNumberOfRefs(6);
                                tv_info.setText(R.string.calculator_txt_payinfo7);
                                tournament = true;
                                cb_sameRegion.setEnabled(true);
                                break;
                        }
                        tv_sum.setText(String.valueOf(sum));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinner.setSelection(spinnerSelection);
                break;
            case 1:     // set travel distances
                if (refList.size() != numOfRefs) {
                    // TODO: improvement by only adding or removing referee objects instead of wiping entire list
                    refList.clear();
                    switch(numOfRefs) {
                        case 3:
                            refList.add(new Referee(Position.R));
                            refList.add(new Referee(Position.U));
                            refList.add(new Referee(Position.LM));
                            break;
                        case 4:
                            refList.add(new Referee(Position.R));
                            refList.add(new Referee(Position.U));
                            refList.add(new Referee(Position.LM));
                            refList.add(new Referee(Position.LJ));
                            break;
                        case 5:
                            refList.add(new Referee(Position.R));
                            refList.add(new Referee(Position.U));
                            refList.add(new Referee(Position.LM));
                            refList.add(new Referee(Position.LJ));
                            refList.add(new Referee(Position.BJ));
                            break;
                        case 6:
                            if (tournament) {
                                refList.add(new Referee(Position.R));
                                refList.add(new Referee(Position.U));
                                refList.add(new Referee(Position.LM));
                                refList.add(new Referee(Position.R));
                                refList.add(new Referee(Position.U));
                                refList.add(new Referee(Position.LM));
                            } else {
                                refList.add(new Referee(Position.R));
                                refList.add(new Referee(Position.U));
                                refList.add(new Referee(Position.LM));
                                refList.add(new Referee(Position.LJ));
                                refList.add(new Referee(Position.SJ));
                                refList.add(new Referee(Position.FJ));
                            }
                            break;
                        case 7:
                            refList.add(new Referee(Position.R));
                            refList.add(new Referee(Position.U));
                            refList.add(new Referee(Position.LM));
                            refList.add(new Referee(Position.LJ));
                            refList.add(new Referee(Position.BJ));
                            refList.add(new Referee(Position.SJ));
                            refList.add(new Referee(Position.FJ));
                            break;
                        case 8:
                            refList.add(new Referee(Position.R));
                            refList.add(new Referee(Position.CJ));
                            refList.add(new Referee(Position.U));
                            refList.add(new Referee(Position.LM));
                            refList.add(new Referee(Position.LJ));
                            refList.add(new Referee(Position.BJ));
                            refList.add(new Referee(Position.SJ));
                            refList.add(new Referee(Position.FJ));
                            break;
                        default:
                            for (int i = 0; i < numOfRefs; i++) {
                                refList.add(new Referee(Position.OTHER));
                            }
                        }
                    }
                    setContentView(R.layout.activity_calculator_distances);

                    containerLayout = findViewById(R.id.referee_distances);

                    for (Referee r: refList) {
                        LinearLayout ll = new LinearLayout(this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        EditText label = new EditText(this);
                        label.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3.f));
                        label.setInputType(InputType.TYPE_CLASS_TEXT);
                        label.setText(r.name);
                        label.setSelectAllOnFocus(true);
                        label.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (!s.toString().equals("")) {
                                    r.name = s.toString();
                                }
                            }
                        });
                        ll.addView(label);

                        EditText value = new EditText(this);
                        value.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.f)); // TODO: set fixed size
                        value.setInputType(InputType.TYPE_CLASS_NUMBER);
                        value.setText(String.valueOf(r.distance));
                        value.setSelectAllOnFocus(true);
                        value.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                try {
                                    r.distance = Integer.parseInt(s.toString(), 10);
                                } catch (Exception e) {
                                    r.distance = 0;
                                }
                                screenProgress = 1;
                            }
                        });
                        ll.addView(value);

                        containerLayout.addView(ll);
                    }
                    if (screenProgress < page)
                        screenProgress = page;
                break;
            case 2:     // final money distribution
                setContentView(R.layout.activity_calculator_lastchanges);

                if (screenProgress < page) {
                    remainingMoney = sum;
                    if (sameRegion) {
                        remainingMoney -= 5;
                        System.out.println(refList.get(0).name + " receives +5 Euros");
                        refList.get(0).regionalMoney = 5;
                    }
                    for (Referee r: refList) {
                        if (r.name.equals(""))
                            r.resetName();
                        r.fare = (int) Math.floor(r.distance * 0.3 / 5) * 5;
                        remainingMoney -= r.fare;
                    }
                    everyoneGets = 5 * (remainingMoney / (numOfRefs * 5));
                    remainingMoney -= everyoneGets * numOfRefs;
                }


                containerLayout = findViewById(R.id.referee_lastchanges);
                tv_remainder = findViewById(R.id.calculator_value_remainder);

                LinearLayout ll_each = new LinearLayout(this);
                ll_each.setOrientation(LinearLayout.HORIZONTAL);
                ll_each.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView everyoneGetsLabel = new TextView(this);
                everyoneGetsLabel.setText(R.string.calculator_txt_eachonegets);

                TextView everyoneGetsValue = new TextView(this);
                everyoneGetsValue.setText(String.valueOf(everyoneGets));

                Button decAllButton = new Button(this);
                decAllButton.setText(R.string.decrease);
                decAllButton.setOnClickListener(v -> {
                    if (everyoneGets > 0) {
                        everyoneGets -= 5;
                        everyoneGetsValue.setText(String.valueOf(everyoneGets));
                        updateRemainderValue();
                    }
                });

                Button incAllButton = new Button(this);
                incAllButton.setText(R.string.increase);
                incAllButton.setOnClickListener(v -> {
                    everyoneGets += 5;
                    everyoneGetsValue.setText(String.valueOf(everyoneGets));
                    updateRemainderValue();
                });

                ll_each.addView(everyoneGetsLabel);
                ll_each.addView(decAllButton);
                ll_each.addView(everyoneGetsValue);
                ll_each.addView(incAllButton);
                containerLayout.addView(ll_each);

                View hl = new View(this);
                hl.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); // TODO: replace getColor()-call; // TODO: getColor() is deprecated!
                hl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
                containerLayout.addView(hl);

                for (int i = 0; i < refList.size(); i++) {
                    Referee r = refList.get(i);
                    if (r.distance > 0 || r.regionalMoney > 0) {       // TODO: feedback einholen zu ALLE immer anzeigen oder nur Fahrer
                        LinearLayout ll = new LinearLayout(this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        TextView refName = new TextView(this);
                        refName.setText(r.name);

                        TextView refAdditionalPayment = new TextView(this);
                        refAdditionalPayment.setText(String.valueOf(r.fare));

                        Button refDec = new Button(this);
                        refDec.setText(R.string.decrease);
                        refDec.setOnClickListener(v -> {
                            System.out.println("+++++++++++++++ Fahrtgeld " + r.name + ": " + r.fare);
                            if (r.fare > 0) {
                                r.fare -= 5;
                                refAdditionalPayment.setText(String.valueOf(r.fare));
                                updateRemainderValue();
                            }
                        });

                        Button refInc = new Button(this);
                        refInc.setText(R.string.increase);
                        refInc.setOnClickListener(v -> {
                            r.fare += 5;
                            refAdditionalPayment.setText(String.valueOf(r.fare));
                            updateRemainderValue();
                        });

                        ll.addView(refName);

                        if (i == 0 && sameRegion) {
                            TextView sameRegion = new TextView(this);
                            sameRegion.setText(R.string.calculator_txt_add5);
                            ll.addView(sameRegion);
                        }

                        ll.addView(refDec);
                        ll.addView(refAdditionalPayment);
                        ll.addView(refInc);
                        containerLayout.addView(ll);
                    }
                }

                updateRemainderValue();
                if (screenProgress < page)
                    screenProgress = page;
                break;
            case 3:
                setContentView(R.layout.activity_calculator_result);


                containerLayout = findViewById(R.id.referee_lastchanges);       // TODO clicking on the view changes the display of one number (total) to split up display (everyone + fare + regional)

                for (Referee r: refList) {
                    LinearLayout ll = new LinearLayout(this);
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    TextView refName = new TextView(this);
                    refName.setText(r.name);

                    TextView refTotalMoney = new TextView(this);
                    refTotalMoney.setText(String.valueOf(r.fare + r.regionalMoney + everyoneGets));

                    ll.addView(refName);
                    ll.addView(refTotalMoney);
                    containerLayout.addView(ll);
                }


                if (screenProgress < page)
                    screenProgress = page;
                break;
            default:
                // TODO: throw error!
                // throw error!
        }
    }

    public void previousActivity(View view) {
        page--;
        updateScreen();
    }

    public void nextActivity(View view) {
        if (page == 2 && remainingMoney != 0) {
            if (remainingMoney > 0)
                Toast.makeText(getApplicationContext(), "Restgeld muss ganz verteilt werden", Toast.LENGTH_SHORT).show(); // TODO String
            if (remainingMoney < 0)
                Toast.makeText(getApplicationContext(), "Zu viel Geld verteilt!", Toast.LENGTH_SHORT).show(); // TODO String
        } else {
            page++;
            updateScreen();
        }
    }

    void updateNumberOfRefs(int numOfRefs) {
        this.numOfRefs = numOfRefs;
        tv_numOfRefs.setText(String.valueOf(numOfRefs));
        updateSum();
    }

    void updateSum() {
        switch (spinnerSelection) {
            case 0:
                sum = 100 + 80 * (numOfRefs - 1);
                break;
            case 1:
                sum = 50 * numOfRefs + 120;
                break;
            case 2:
                sum = 40 * numOfRefs + 120;
        }
        tv_sum.setText(String.valueOf(sum));
    }

    public void decreaseReferees(View view) {
        screenProgress = 0;
        if (numOfRefs > 3)
            updateNumberOfRefs(numOfRefs - 1);
    }

    public void increaseReferees(View view) {
        screenProgress = 0;
        if (numOfRefs < 8)
            updateNumberOfRefs(numOfRefs + 1);
    }

    void updateSum(int sum) {
        this.sum = sum;
        tv_sum.setText(String.valueOf(sum));
    }

    public void decreaseSum(View view) {
        screenProgress = 0;
        if (sum > 5)
            updateSum(sum - 5);
    }

    public void increaseSum(View view) {
        screenProgress = 0;
        updateSum(sum + 5);
    }

    public void updateRemainderValue() {
        int money = 0;
        for (Referee r: refList) {
            money += r.fare + r.regionalMoney + everyoneGets;
        }
        remainingMoney = sum-money;
        tv_remainder.setText(String.valueOf(remainingMoney));
    }

    public void sameRegionChecked(View view) {
        sameRegion = !sameRegion;
        cb_sameRegion.setChecked(sameRegion);
        if (!sameRegion && refList.size() > 0) {
            refList.get(0).regionalMoney = 0;
        }
    }
}