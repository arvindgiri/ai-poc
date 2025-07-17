# Hello World Transformer

A basic transformer implementation from scratch in PyTorch, designed for educational purposes. This implementation includes all the core components of a transformer encoder: multi-head attention, positional encoding, and feed-forward networks.

## Features

- **Multi-Head Attention**: The core mechanism that allows the model to attend to different parts of the sequence simultaneously
- **Positional Encoding**: Adds position information to embeddings since transformers don't have inherent sequence order
- **Feed-Forward Networks**: Position-wise transformations applied to each token
- **Layer Normalization**: Stabilizes training and improves convergence
- **Residual Connections**: Helps with gradient flow in deep networks

## Installation

```bash
pip install -r requirements.txt
```

## Usage

### Basic Example

```python
from simple_transformer import SimpleTransformer
import torch

# Model hyperparameters
vocab_size = 1000      # Size of vocabulary
d_model = 512          # Embedding dimension
num_heads = 8          # Number of attention heads
num_layers = 6         # Number of encoder layers
d_ff = 2048           # Feed-forward dimension
max_seq_length = 100   # Maximum sequence length

# Create the model
model = SimpleTransformer(vocab_size, d_model, num_heads, num_layers, d_ff, max_seq_length)

# Example input (batch_size=2, seq_length=10)
src = torch.randint(0, vocab_size, (2, 10))

# Forward pass
output = model(src)
print(f"Output shape: {output.shape}")  # [2, 10, 512]
```

### Running the Demo

```bash
python simple_transformer.py
```

This will run a demonstration showing:
- Input and output shapes
- Number of model parameters
- Example classification usage

## Architecture Components

### 1. Positional Encoding
Adds positional information using sine and cosine functions:
- Even indices: sin(pos/10000^(2i/d_model))
- Odd indices: cos(pos/10000^(2i/d_model))

### 2. Multi-Head Attention
Implements scaled dot-product attention with multiple heads:
- Query, Key, Value projections
- Scaled attention scores
- Multi-head concatenation

### 3. Feed-Forward Network
Two linear transformations with ReLU activation:
- Linear(d_model → d_ff) → ReLU → Linear(d_ff → d_model)

### 4. Encoder Layer
Combines attention and feed-forward with residual connections:
- Self-attention + residual + layer norm
- Feed-forward + residual + layer norm

## Model Parameters

With default settings (d_model=512, num_heads=8, num_layers=6):
- Total parameters: ~25M
- Embedding layer: ~512K parameters
- Each encoder layer: ~4M parameters

## Extending the Model

### For Classification Tasks
```python
# Add classification head
classifier = nn.Linear(d_model, num_classes)
cls_output = classifier(output[:, 0, :])  # Use first token
```

### For Sequence-to-Sequence Tasks
- Add decoder layers with cross-attention
- Implement causal masking for autoregressive generation

### For Different Domains
- Adjust vocab_size for your tokenizer
- Modify d_model and num_layers based on task complexity
- Add domain-specific preprocessing

## Educational Notes

This implementation prioritizes clarity over optimization. For production use, consider:
- Using `torch.nn.TransformerEncoder` for better performance
- Implementing more efficient attention mechanisms
- Adding gradient checkpointing for memory efficiency
- Using mixed precision training

## Alternative Libraries

For production use, consider these well-optimized alternatives:
- **Hugging Face Transformers**: Pre-trained models and easy fine-tuning
- **fairseq**: Facebook's sequence modeling toolkit
- **torch.nn.Transformer**: PyTorch's built-in transformer implementation

## References

- [Attention Is All You Need](https://arxiv.org/abs/1706.03762) - Original transformer paper
- [The Illustrated Transformer](https://jalammar.github.io/illustrated-transformer/) - Visual explanation
- [PyTorch Transformer Tutorial](https://pytorch.org/tutorials/beginner/transformer_tutorial.html) - Official PyTorch tutorial 