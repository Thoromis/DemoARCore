package mc.fhooe.at.demoarcore;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArFragment arFragment;
    private ModelRenderable bearRenderable, dogRenderable;

    ImageView bear, dog;

    View arrayView[];
    int selected = 1;

    ViewRenderable animal_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);

        bear = findViewById(R.id.bear);
        dog = findViewById(R.id.dog);
        setUpModel();
        setArrayView();
        setClickListener();
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                createModel(anchorNode, selected);

            }
        });

    }

    private void setUpModel() {

        ModelRenderable.builder()
                .setSource(this, R.raw.bear)
                .build().thenAccept(renderable -> bearRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load bear model", Toast.LENGTH_SHORT).show();
                            return null;

                        }
                );
        ModelRenderable.builder()
                .setSource(this, R.raw.dog)
                .build().thenAccept(renderable -> dogRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load dog model", Toast.LENGTH_SHORT).show();
                            return null;

                        }
                );

    }

    private void createModel(AnchorNode anchorNode, int selected) {
        switch (selected) {
            case 1: {
                TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
                bear.setParent(anchorNode);
                bear.setRenderable(bearRenderable);
                bear.select();

                addName(anchorNode, bear, "Bear");
            }
            break;
            case 2: {
                TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
                bear.setParent(anchorNode);
                bear.setRenderable(dogRenderable);
                bear.select();

                addName(anchorNode, bear, "Dog");
            }
            break;
        }
    }

    private void addName(AnchorNode anchorNode, TransformableNode model, String name) {

        ViewRenderable.builder()
                .setView(this, R.layout.animal_name)
                .build()
                .thenAccept(viewRenderable -> {
                    TransformableNode nameView = new TransformableNode(arFragment.getTransformationSystem());
                    nameView.setLocalPosition(new Vector3(0f, model.getLocalPosition().y + 0.5f, 0));
                    nameView.setParent(anchorNode);
                    nameView.setRenderable(viewRenderable);
                    nameView.select();

                    TextView txt_name = (TextView) viewRenderable.getView();
                    txt_name.setText(name);

                    txt_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            anchorNode.setParent(null);
                        }
                    });
                });

    }

    private void setClickListener() {
        for (int i = 0; i < arrayView.length; i++) {
            arrayView[i].setOnClickListener(this);
        }
    }

    private void setArrayView() {
        arrayView = new View[]{
                bear, dog
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bear: {
                selected = 1;
                setBackbround(view.getId());
            }
            break;
            case R.id.dog: {
                selected = 4;
                setBackbround(view.getId());
            }
        }
    }

    private void setBackbround(int id) {
        for (int i = 0; i < arrayView.length; i++) {
            if (arrayView[i].getId() == id) {
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"));
            } else {
                arrayView[i].setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
