package mc.fhooe.at.demoarcore;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArFragment arFragment;
    private ModelRenderable bearRenderable, dogRenderable, hippoRenderable;

    ImageView bear, dog, hippo;

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
        hippo = findViewById(R.id.hippo);
        setUpModel();
        setArrayView();
        setClickListener();
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());

            createModel(anchorNode, selected);

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
        ModelRenderable.builder()
                .setSource(this, R.raw.hippopotamus)
                .build().thenAccept(renderable -> hippoRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load hippo model", Toast.LENGTH_SHORT).show();
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
            case 3: {
                TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
                bear.setParent(anchorNode);
                bear.setRenderable(hippoRenderable);
                bear.select();

                addName(anchorNode, bear, "Hippo");
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

                    txt_name.setOnClickListener(view -> anchorNode.setParent(null));
                });

    }

    private void setClickListener() {
        for (View anArrayView : arrayView) {
            anArrayView.setOnClickListener(this);
        }
    }

    private void setArrayView() {
        arrayView = new View[]{
                bear, dog, hippo
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bear: {
                selected = 1;
                setBackground(view.getId());
            }
            break;
            case R.id.dog: {
                selected = 2;
                setBackground(view.getId());
            }
            break;
            case R.id.hippo: {
                selected = 3;
                setBackground(view.getId());
            }
            break;
        }
    }

    private void setBackground(int id) {
        for (View anArrayView : arrayView) {
            if (anArrayView.getId() == id) {
                anArrayView.setBackgroundColor(Color.parseColor("#80333639"));
            } else {
                anArrayView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
