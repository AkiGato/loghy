# Neumorphism Mixed with Ink Design System

The **Neumorphism Mixed with Ink** design system blends soft, extruded paper physical geometry with the dynamic, expressive fluidity of fountain pen ink. It avoids cold digital minimalism and plastic skeuomorphism in favor of a warm, tactile, distraction-free environment for deep thinking.

---

## 1. Design Philosophy

- **Tactile Paper Surfaces**: UI elements are pressed, extruded, or etched into rich textured card stock.
- **Organic Fluid Ink**: Visual accents, indicators, and the companion avatar behave like dynamic, viscous ink droplets possessing mass, surface tension, and kinetic resonance.
- **Physical Feedback**: Every interaction mimics physical desk tools — recessed letterpress trays, elevated stamps, and damp ink drying on cotton paper.

---

## 2. Neumorphic Dual-Lighting Engine

Neumorphic depth in Loghy is constructed using twin directional light vectors:

```
           [ Virtual Light Source: Top-Left (-135°) ]
                           \
                            v
               +-------------------------+  <-- Soft Highlight (White/Cream)
               |                         |
               |      Tactile Card       |
               |                         |
               +-------------------------+  <-- Deep Diffuse Shadow (Ink/Charcoal)
```

### Depth Topologies

| Component | Physical Metaphor | Lighting Configuration |
|---|---|---|
| **`NeumorphicRaisedBox`** | Elevated paper tile / stamp | Top-left specular bloom + Bottom-right soft shadow |
| **`NeumorphicDebossedBox`** | Recessed tray / letterpress groove | Top-left inner shadow + Bottom-right subtle edge glow |
| **`NeumorphicButton`** | Tactile mechanical key | Convex rest state (3dp) that presses flush (0.5dp) on tap |

---

## 3. The Ink Fluidity Layer

Contrasting the quiet stillness of paper, ink elements feature living fluid dynamics:

### A. The Fluid Companion (`FluidCreature`)
- Rendered on a hardware-accelerated Compose `Canvas`.
- Implements cubic Bézier spline interpolation across 8 radial control points.
- Modulates harmonic sine oscillations to simulate breathing, squash-and-stretch curiosity, and reactive jiggles on touch.

### B. Living Droplet Bullet Points (`FluidBulletDroplet`)
- Organic ink beads marking Socratic takeaways.
- Rendered with radial gradients simulating surface tension, an internal light refraction core, and a soft micro-shadow.

### C. Undulating Ink Underlines (`UndulatingInkUnderline`)
- Hand-drawn wet ink strokes under section headers.
- Uses dynamic cubic Bézier wave calculations with subtle thickness jitter to evoke calligraphic pen pressure.
---

Design System: Neumorphic Ink
Core Philosophy
The visual language merges the tactile, soft-extruded surface quality of neumorphism with the sharp, high-contrast clarity of traditional ink on paper. Surfaces feel pressed or lifted from a physical parchment sheet, while interactive elements react with weighted, organic fluidity.
Color Palette
| Token | Value | Role |
|---|---|---|
| Paper Base | #F4F0EA | Primary background and surface color simulating aged parchment. |
| Ink Black | #0B0A09 | Primary text, active states, borders, and solid structural lines. |
| Ghost Ink | rgba(11, 10, 9, 0.7) | Secondary text, subtle borders, and unselected structural elements. |
Typography & Content Principles
 * Uniqueness: The exact same text string never repeats within the same view or component state. Copywriting relies on dynamic variation.
 * Hierarchy: Set exclusively in high-contrast Ink Black and Ghost Ink to maintain a crisp print-like legibility against the paper background.
 * Prohibition: Zero decorative emojis anywhere in the interface.
Iconography
 * Style: Monochromatic vector graphics rendered strictly in solid Ink Black fills or sharp Ghost Ink outlines.
 * Weight: Consistent stroke widths that mimic fine-nib pen illustrations. No gradients, shadows, or multi-color fills inside icons.
Motion & Interaction Architecture
 * Fluid Transitions: All state changes utilize cubic-bezier curves mimicking physical momentum and resistance.
 * Component State Logic: The core control mechanism manages class toggles and positional transforms for dynamic elements such as radio selectors and sliding indicators:
let radios = document.querySelectorAll('.radio');
let labels = document.querySelectorAll('.label');
let ball = document.querySelector('.ball');
let prevRadio, prevLabel;

radios.forEach((radio, index) => {
  radio.addEventListener('click', function(e) {
    if (prevRadio) prevRadio.classList.toggle('active');
    if (prevLabel) prevLabel.classList.toggle('active');
    radio.classList.toggle('active');
    prevRadio = radio;
    labels[index].classList.toggle('active');
    prevLabel = labels[index];
    ball.className = ball pos${index};
  });
});

 * Surface Depth: Neumorphic shadow layers combine dual offset shadows (one light highlight, one deep ink shadow) to create a sunken or raised paper effect that dynamically shifts during active states.