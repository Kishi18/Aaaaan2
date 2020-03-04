package com.teamproject.aaaaan_2.ui.mini_game_tabfrag;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamproject.aaaaan_2.R;


public class RouletteGame extends Fragment {
    View view;

    private ImageView mImageView;
    private Button rotate;


    private Bitmap mBitmap;
    private float angle = 0.0f; // 초기 각도
    private float fromAngle = 0.0f;
    private final int IMG_DP = 300; // 이미지 DP
    RotateAnimation animation;

    TextView result_text;
    Button select_btn;
    EditText peo_num;

    int su;
    String result;

    public RouletteGame() {
        // Required empty public constructor
    }

    public static RouletteGame newInstance() {
        RouletteGame tab1 = new RouletteGame();
        return tab1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_roulette, container, false);

        //이미지뷰
        mImageView = view.findViewById(R.id.wheel);
        //결과값이 출력될곳 (텍스트뷰)
        result_text = view.findViewById(R.id.result_text);
        //게임인원 지정할 에디트 텍스트
        peo_num = view.findViewById(R.id.peo_num);
        //인원수를 확정지을 버튼
        select_btn = view.findViewById(R.id.select_btn);
        //룰렛을 회전시키기위한 애니메이션
        animation = new RotateAnimation(angle, fromAngle
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


        //이미지 저장공간(?)
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.roulette_4);

        //이미지뷰에 bitmap에 저장된 이미지를 띄운다
        mImageView.setImageBitmap(onResizeImage(mBitmap));

        //회전 버튼
        rotate = view.findViewById(R.id.start_btn);
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //0으로 초기화 한 이유는 게임을 다시 시작할때 그전에 나온 결과값을 초기화 하기 위하여
                fromAngle = 0;

                clear_angle(animation);
                //aniamtion 재정의 한 이유 = 룰렛을 한번돌린뒤 animation을 그대로 사용하면 룰렛이 그냥 돌아간다. 따라서 pivotY값을 0으로 줘서 재정의(?) 한다
                mImageView.startAnimation(animation = new RotateAnimation(angle, fromAngle
                        , Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f));
                onWheelImage();
            }

        });

        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_angle(animation);
                mImageView.startAnimation(animation = new RotateAnimation(angle, fromAngle
                        , Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f));

                // EditText에 입력한 결과에 맞는 이미지를 불러오기위해
                su = Integer.parseInt(peo_num.getText().toString());
                if (su == 4) {
                    mImageView.setImageResource(R.drawable.roulette_4);
                } else if (su == 5) {
                    mImageView.setImageResource(R.drawable.roulette_55);
                } else if (su == 6) {
                    mImageView.setImageResource(R.drawable.roulette_6);
                } else if (su == 7) {
                    mImageView.setImageResource(R.drawable.roulette_7);
                } else if (su == 8) {
                    mImageView.setImageResource(R.drawable.roulette_8);
                } else {
                    Toast.makeText(getContext(), "4~8사이의 숫자만 입력해주세요", Toast.LENGTH_SHORT).show();
                    mImageView.setImageResource(0);
                }
            }
        });


        return view;
    }


    /**
     * from DP to Pixel
     *
     * @param dp
     * @param context
     * @return
     */
    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }


    /**
     * image resizing
     *
     * @param bitmap
     * @return
     */
    private Bitmap onResizeImage(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Float size = convertDpToPixel(IMG_DP, getContext());

        Bitmap resized = null;
        while (height > size.intValue()) {
            resized = Bitmap.createScaledBitmap(bitmap, (width * size.intValue()) / height, size.intValue(), true);
            height = resized.getHeight();
            width = resized.getWidth();
        }
        return resized;
    }

    private int getRandom(int max) {
        return (int) (Math.random() * max);
    }

    private void onWheelImage() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int random = getRandom(360);
                fromAngle = random + 720 + angle; // 회전수 제어(랜덤(0~360)+720도+회전각)
                Log.e("랜덤값", Float.toString(fromAngle));

                // 로테이션 애니메이션 초기화
                // 시작각, 종료각, 자기 원을 그리며 회전 옵
                // RELATIVE_TO_SELF 뷰 자신의 크기를 기준으로 이동
                animation = new RotateAnimation(angle, fromAngle
                        , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                //각도에 따른 조건을 메소드로 따로 빼 놓은것..
                getTargetEvent(fromAngle);



                // 초기 시작각도 업데이트
                clear_angle(animation);
                mImageView.startAnimation(animation);

            }


        });
    }


    private void getTargetEvent(float value) {
        su = Integer.parseInt(peo_num.getText().toString());
        Log.e("angle", "ngle" + angle);
        Log.e("fromangle", "fromangle" + fromAngle);
        Log.e("value", "valued = " + value);
        if (su == 4) {
            if ((value >= 720 && value <= 765) || (value >= 1036 && value <= 1125)) {
                result = "4번칸";
                Toast.makeText(getContext(), "4번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);
            } else if (value >= 766 && value <= 855 || (value >= 1126 && value <= 1170)) {
                result = "3번칸";
                Toast.makeText(getContext(), "3번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);
            } else if (value >= 856 && value <= 945) {
                result = "2번칸";
                Toast.makeText(getContext(), "2번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);
            } else if (value >= 946 && value <= 1035) {
                Toast.makeText(getContext(), "1번칸", Toast.LENGTH_SHORT).show();
                result = "1번칸";
                result_text.setText(result);
            }
        } else if (su == 5) {
            if (value >= 720 && value <= 792 || (value >= 1080 && value <= 1116)) {
                result = "5번칸";
                Toast.makeText(getContext(), "5번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);
            } else if ((value >= 793 && value <= 864)) {
                result = "4번칸";
                Toast.makeText(getContext(), "4번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);

            } else if (value >= 865 && value <= 936) {
                result = "3번칸";
                Toast.makeText(getContext(), "3번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);

            } else if (value >= 937 && value <= 1006) {
                Toast.makeText(getContext(), "2번칸", Toast.LENGTH_SHORT).show();
                result = "2번칸";
                result_text.setText(result);
            } else if (value >= 1007 && value <= 1079) {
                Toast.makeText(getContext(), "1번칸", Toast.LENGTH_SHORT).show();
                result = "1번칸";
                result_text.setText(result);
            }
        } else if (su == 6) {
            if ((value >= 720 && value <= 748)|| (value >= 1048 && value <= 1080)) {
                result = "5번칸";
                Toast.makeText(getContext(), "5번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);
            } else if (value >= 749 && value <= 809 ) {
                result = "4번칸";
                Toast.makeText(getContext(), "4번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);
            } else if (value >= 810 && value <= 871) {
                result = "3번칸";
                Toast.makeText(getContext(), "3번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);

            } else if (value >= 872 && value <= 931) {
                Toast.makeText(getContext(), "2번칸", Toast.LENGTH_SHORT).show();
                result = "2번칸";
                result_text.setText(result);
            } else if (value >= 932 && value <= 989) {
                Toast.makeText(getContext(), "1번칸", Toast.LENGTH_SHORT).show();
                result = "1번칸";
                result_text.setText(result);
            }else if (value >= 990 && value <= 1047) {
                Toast.makeText(getContext(), "6번칸", Toast.LENGTH_SHORT).show();
                result = "6번칸";
                result_text.setText(result);
            }
        } else if (su == 7) {
            if ((value >= 720 && value <= 770) || (value >= 1081 && value <= 1131)) {
                result = "7번칸";
                Toast.makeText(getContext(), "7번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);
            } else if (value >= 771 && value <= 823) {
                result = "6번칸";
                Toast.makeText(getContext(), "6번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);

            } else if (value >= 824 && value <= 874) {
                result = "5번칸";
                Toast.makeText(getContext(), "5번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);

            } else if (value >= 875 && value <= 926) {
                Toast.makeText(getContext(), "4번칸", Toast.LENGTH_SHORT).show();
                result = "4번칸";
                result_text.setText(result);
            } else if (value >= 927 && value <= 976) {
                Toast.makeText(getContext(), "3번칸", Toast.LENGTH_SHORT).show();
                result = "3번칸";
                result_text.setText(result);
            } else if (value >= 977 && value <= 1027) {
                Toast.makeText(getContext(), "2번칸", Toast.LENGTH_SHORT).show();
                result = "2번칸";
                result_text.setText(result);
            } else if (value >= 1028 && value <= 1080) {
                Toast.makeText(getContext(), "1번칸", Toast.LENGTH_SHORT).show();
                result = "1번칸";
                result_text.setText(result);
            }
        } else if (su == 8) {
            if ((value >= 720 && value <= 765) || (value >= 1081 && value <= 1125)) {
                result = "8번칸";
                Toast.makeText(getContext(), "8번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);
            } else if (value >= 766 && value <= 810) {
                result = "7번칸";
                Toast.makeText(getContext(), "7번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);

            } else if (value >= 811 && value <= 855) {
                result = "6번칸";
                Toast.makeText(getContext(), "6번칸", Toast.LENGTH_SHORT).show();
                result_text.setText(result);

            } else if (value >= 856 && value <= 900) {
                Toast.makeText(getContext(), "5번칸", Toast.LENGTH_SHORT).show();
                result = "5번칸";
                result_text.setText(result);
            } else if (value >= 901 && value <= 945) {
                Toast.makeText(getContext(), "4번칸", Toast.LENGTH_SHORT).show();
                result = "4번칸";
                result_text.setText(result);
            } else if (value >= 946 && value <= 990) {
                Toast.makeText(getContext(), "3번칸", Toast.LENGTH_SHORT).show();
                result = "3번칸";
                result_text.setText(result);
            } else if (value >= 991 && value <= 1035) {
                Toast.makeText(getContext(), "2번칸", Toast.LENGTH_SHORT).show();
                result = "2번칸";
                result_text.setText(result);
            } else if (value >= 1036 && value <= 1080) {
                Toast.makeText(getContext(), "1번칸", Toast.LENGTH_SHORT).show();
                result = "1번칸";
                result_text.setText(result);
            }
        }
    }

    private void clear_angle(RotateAnimation animation) {
        angle = fromAngle;
        animation.setDuration(2000); // 지속시간이 길수록 느려짐
        animation.setFillEnabled(true); // 애니메이션 종료된 후 상태 고정 옵션
        animation.setFillAfter(true); //
        /*mImageView.startAnimation(animation);*/
    }

}