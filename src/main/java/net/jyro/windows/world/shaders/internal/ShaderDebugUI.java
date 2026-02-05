package net.jyro.windows.world.shaders.internal;

import net.jyro.windows.world.shaders.sims.PlanetaryShader;
import org.joml.Vector3f;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ShaderDebugUI {

    // Variables storing current slider values
    public volatile float densityFalloff = 0.5f;
    public volatile int numInScattering = 32;
    public volatile int numOpticalDepthPoints = 32;
    public volatile float intensity = 1.0f;
    public volatile Vector3f scatteringCoefficients = new Vector3f(0.0055f, 0.013f, 0.0224f);
    public volatile float ditherStrength = 0.01f;
    public volatile float ditherScale = 1.0f;

    public volatile float planetRadius = 1.0f;
    public volatile float oceanRadius = 0.0f;
    public volatile float atmosphereRadius = 1.2f;

    public volatile Vector3f surfaceColor = new Vector3f(0.6f, 0.5f, 0.3f);
    public volatile Vector3f atmosphereColor = new Vector3f(0.5f, 0.7f, 1.0f);

    public ShaderDebugUI() {
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("Planetary Shader Debug");
        frame.setLayout(new GridLayout(0,1));

        // --- Density Falloff ---
        JSlider densitySlider = new JSlider(1, 500, (int)(densityFalloff * 100f));
        densitySlider.addChangeListener(e -> {
            densityFalloff = densitySlider.getValue() / 100f;
        });
        frame.add(new JLabel("Density Falloff"));
        frame.add(densitySlider);

        // --- Number of In-Scattering Steps ---
        JSlider inScatSlider = new JSlider(1, 128, numInScattering);
        inScatSlider.addChangeListener(e -> {
            numInScattering = inScatSlider.getValue();
        });
        frame.add(new JLabel("In-Scattering Steps"));
        frame.add(inScatSlider);

        // --- Intensity ---
        JSlider intensitySlider = new JSlider(1, 100, (int)(intensity * 10f));
        intensitySlider.addChangeListener(e -> {
            intensity = intensitySlider.getValue() / 10f;
        });
        frame.add(new JLabel("Intensity"));
        frame.add(intensitySlider);


        // --- Planet Radii ---
        JSlider planetRadiusSlider = new JSlider(1, 500, (int)(planetRadius*100));
        planetRadiusSlider.addChangeListener(e -> {
            planetRadius = planetRadiusSlider.getValue() / 100f;
        });
        frame.add(new JLabel("Planet Radius"));
        frame.add(planetRadiusSlider);

        JSlider atmosphereRadiusSlider = new JSlider(1, 500, (int)(atmosphereRadius*100));
        atmosphereRadiusSlider.addChangeListener(e -> {
            atmosphereRadius = atmosphereRadiusSlider.getValue() / 100f;
        });
        frame.add(new JLabel("Atmosphere Radius"));
        frame.add(atmosphereRadiusSlider);

        // --- Colors ---
        JButton surfaceColorBtn = new JButton("Surface Color");
        surfaceColorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(frame, "Select Surface Color", new Color(surfaceColor.x, surfaceColor.y, surfaceColor.z));
            if (c != null) {
                surfaceColor.set(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f);
            }
        });
        frame.add(surfaceColorBtn);

        JButton atmosphereColorBtn = new JButton("Atmosphere Color");
        atmosphereColorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(frame, "Select Atmosphere Color", new Color(atmosphereColor.x, atmosphereColor.y, atmosphereColor.z));
            if (c != null) {
                atmosphereColor.set(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f);
            }
        });
        frame.add(atmosphereColorBtn);

        frame.pack();
        frame.setVisible(true);
    }
}
