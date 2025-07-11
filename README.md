# Tetris Game Implementation

## Overview
A Java implementation of the classic Tetris game featuring a functional game board, piece manipulation system, and an AI brain that plays autonomously. The project implements the Super Rotation System (SRS) for piece rotations and wall kicks, supporting all seven standard tetrominoes with unique colors and behaviors.

## Features

### Core Components
- **TetrisPiece**: Handles all seven tetromino types with rotation logic
- **TetrisBoard**: Manages game state, piece placement, and row clearing
- **AI Brain (CoolAlgo)**: Autonomous player achieving average scores of ~142
- **Wall Kicks**: Full SRS implementation for advanced piece rotation

### Game Controls
- `A` - Move left
- `D` - Move right
- `S` - Move down
- `Q` - Rotate counterclockwise
- `E` - Rotate clockwise
- `W` - Drop piece

## Technical Implementation

### TetrisPiece Class
- Implements circular linked list for constant-time rotation access
- Manages bounding boxes and skirt calculations
- Handles body coordinates relative to piece origin
- Special handling for edge cases (Stick piece 4x4 box, Square piece 2x2 box)

### TetrisBoard Class
- Efficient grid management with constant-time accessor methods
- Action/Result abstraction for move validation
- Automatic row clearing with proper block settling
- Wall kick implementation for all piece types

### AI Brain Algorithm
The AI uses a weighted scoring system evaluating:
- **Sum of column heights** (weight: -1.075)
- **Number of holes** (weight: -2.79)
- **Board roughness** (weight: -0.37)
- **Relative height difference** (weight: -1.39532)
- **Rows cleared** (weight: +175.5)
- **Maximum height** (weight: -1.495)

Formula: `100 - (weighted negative features) + (weighted positive features)`

## Testing Strategy

### JUnit Test Coverage
- **TetrisPiece**: All methods tested with edge cases for Stick and Square pieces
- **TetrisBoard**: Complex board scenarios including overhangs and wall kicks
- **Brain**: Performance testing over 100 runs, achieving 78% success rate for scores >100

### Edge Cases Handled
- Odd-sized bounding boxes (Stick: 4x4, Square: 2x2)
- Wall kick scenarios at borders and between pieces
- Piece placement under overhangs
- Row clearing with floating blocks

## Results & Performance

### AI Performance Statistics
- **Average Score**: 142
- **Standard Deviation**: 51
- **Success Rate**: 78% (scoring over 100)
- **High Score**: Demonstrated in testing documentation

### Interesting Findings
1. AI performs optimally with 0 delay (contrary to human behavior)
2. Adding more features to scoring algorithm decreased performance
3. Custom AI unexpectedly outperformed baseline Rover implementation

## Educational Impact
- **Skill Development**: Enhances hand-eye coordination and spatial reasoning
- **Business Applications**: Teaches space optimization concepts
- **Accessibility**: Benefits users learning pattern recognition

## Limitations
- No visual instructions for controls
- Keyboard-only interface (no mouse support)
- Fixed control scheme may confuse new users
- No difficulty settings or tutorials

## Technical Challenges Overcome
1. Initial piece visibility issues - Fixed constructor parameters
2. Single piece generation bug - Removed static instance variables
3. Diagonal falling pieces - Corrected action execution order
4. Git branch divergence - Implemented divide-and-conquer workflow
5. Static array access issues - Refactored Board class implementation

## Pair Programming Statistics
- **Total Hours**: 84 (42 hours per person)
- **Ari**: 20 hours driving, 18 hours navigating, 5 hours solo
- **Smruti**: 18 hours driving, 20 hours navigating, 3 hours solo

### Pair Programming Insights
**Benefits**:
- Collaborative problem-solving
- Knowledge sharing and concept clarification
- Task delegation based on strengths

**Challenges**:
- Ensuring equal contribution
- Communication barriers when navigating
- Initial workflow coordination

## Authors
- Smruti Sannabhadti
- Ari Ayzenberg