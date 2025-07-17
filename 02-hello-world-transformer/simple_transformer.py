import torch
import torch.nn as nn
import torch.nn.functional as F
import math

class PositionalEncoding(nn.Module):
  """
  Adds positional information to input embeddings since transformers don't have inherent sequence order
  """
  def __init__(self, d_model, max_seq_length=5000):
    super().__init__()
    
    # Create positional encoding matrix
    pe = torch.zeros(max_seq_length, d_model)
    position = torch.arange(0, max_seq_length, dtype=torch.float).unsqueeze(1)
    
    # Create division term for sine and cosine functions
    div_term = torch.exp(torch.arange(0, d_model, 2).float() * (-math.log(10000.0) / d_model))
    
    # Apply sine to even indices and cosine to odd indices
    pe[:, 0::2] = torch.sin(position * div_term)
    pe[:, 1::2] = torch.cos(position * div_term)
    
    # Add batch dimension and register as buffer (not a parameter)
    pe = pe.unsqueeze(0).transpose(0, 1)
    self.register_buffer('pe', pe)
    
  def forward(self, x):
    # Add positional encoding to input embeddings
    return x + self.pe[:x.size(0), :].to(x.device)

class MultiHeadAttention(nn.Module):
  """
  Multi-head attention mechanism - the core of transformer architecture
  """
  def __init__(self, d_model, num_heads):
    super().__init__()
    assert d_model % num_heads == 0
    
    self.d_model = d_model
    self.num_heads = num_heads
    self.d_k = d_model // num_heads  # Dimension of each head
    
    # Linear layers for query, key, value projections
    self.w_q = nn.Linear(d_model, d_model)
    self.w_k = nn.Linear(d_model, d_model)
    self.w_v = nn.Linear(d_model, d_model)
    self.w_o = nn.Linear(d_model, d_model)  # Output projection
    
  def scaled_dot_product_attention(self, Q, K, V, mask=None):
    # Calculate attention scores
    attn_scores = torch.matmul(Q, K.transpose(-2, -1)) / math.sqrt(self.d_k)
    
    # Apply mask if provided (for padding or future tokens)
    if mask is not None:
      attn_scores = attn_scores.masked_fill(mask == 0, -1e9)
    
    # Apply softmax to get attention weights
    attn_probs = torch.softmax(attn_scores, dim=-1)
    
    # Apply attention weights to values
    output = torch.matmul(attn_probs, V)
    return output
    
  def forward(self, query, key, value, mask=None):
    batch_size = query.size(0)
    
    # Linear projections and reshape for multi-head attention
    Q = self.w_q(query).view(batch_size, -1, self.num_heads, self.d_k).transpose(1, 2)
    K = self.w_k(key).view(batch_size, -1, self.num_heads, self.d_k).transpose(1, 2)
    V = self.w_v(value).view(batch_size, -1, self.num_heads, self.d_k).transpose(1, 2)
    
    # Apply attention
    attn_output = self.scaled_dot_product_attention(Q, K, V, mask)
    
    # Concatenate heads and put through final linear layer
    attn_output = attn_output.transpose(1, 2).contiguous().view(batch_size, -1, self.d_model)
    output = self.w_o(attn_output)
    
    return output

class FeedForward(nn.Module):
  """
  Position-wise feed-forward network - applies same transformation to each position
  """
  def __init__(self, d_model, d_ff):
    super().__init__()
    self.linear1 = nn.Linear(d_model, d_ff)
    self.linear2 = nn.Linear(d_ff, d_model)
    self.relu = nn.ReLU()
    
  def forward(self, x):
    # Two linear transformations with ReLU activation
    return self.linear2(self.relu(self.linear1(x)))

class EncoderLayer(nn.Module):
  """
  Single encoder layer with multi-head attention and feed-forward network
  """
  def __init__(self, d_model, num_heads, d_ff, dropout):
    super().__init__()
    self.self_attn = MultiHeadAttention(d_model, num_heads)
    self.feed_forward = FeedForward(d_model, d_ff)
    self.norm1 = nn.LayerNorm(d_model)
    self.norm2 = nn.LayerNorm(d_model)
    self.dropout = nn.Dropout(dropout)
    
  def forward(self, x, mask):
    # Self-attention with residual connection and layer norm
    attn_output = self.self_attn(x, x, x, mask)
    x = self.norm1(x + self.dropout(attn_output))
    
    # Feed-forward with residual connection and layer norm
    ff_output = self.feed_forward(x)
    x = self.norm2(x + self.dropout(ff_output))
    
    return x

class SimpleTransformer(nn.Module):
  """
  A basic transformer encoder for sequence processing
  """
  def __init__(self, vocab_size, d_model, num_heads, num_layers, d_ff, max_seq_length, dropout=0.1):
    super().__init__()
    
    # Embedding layer to convert tokens to vectors
    self.embedding = nn.Embedding(vocab_size, d_model)
    self.positional_encoding = PositionalEncoding(d_model, max_seq_length)
    
    # Stack of encoder layers
    self.encoder_layers = nn.ModuleList([
      EncoderLayer(d_model, num_heads, d_ff, dropout) 
      for _ in range(num_layers)
    ])
    
    self.dropout = nn.Dropout(dropout)
    
  def forward(self, src, src_mask=None):
    # Convert tokens to embeddings and add positional encoding
    seq_length = src.size(1)
    src_embedded = self.embedding(src) * math.sqrt(self.embedding.embedding_dim)
    src_embedded = self.positional_encoding(src_embedded)
    src_embedded = self.dropout(src_embedded)
    
    # Pass through encoder layers
    output = src_embedded
    for encoder_layer in self.encoder_layers:
      output = encoder_layer(output, src_mask)
    
    return output

def test_next_word_prediction(model, vocab_size, d_model):
  """
  Test the transformer's ability to predict the next word in a sequence
  """
  print("Creating a smaller model specifically for next word prediction...")
  
  # Create a smaller, more focused model for this task
  small_model = SimpleTransformer(
    vocab_size=20,      # Smaller vocabulary
    d_model=64,         # Smaller embedding dimension
    num_heads=4,        # Fewer attention heads
    num_layers=2,       # Fewer layers
    d_ff=128,          # Smaller feed-forward dimension
    max_seq_length=10,  # Shorter sequences
    dropout=0.1
  )
  
  # Add a language modeling head for next word prediction
  lm_head = nn.Linear(64, 20)  # Match the smaller model dimensions
  
  # Create a simple vocabulary mapping for demonstration
  vocab = {
    '<PAD>': 0, '<UNK>': 1, '<START>': 2, '<END>': 3,
    'the': 4, 'cat': 5, 'sat': 6, 'on': 7, 'mat': 8,
    'dog': 9, 'ran': 10, 'fast': 11, 'bird': 12, 'flew': 13,
    'high': 14, 'tree': 15, 'is': 16, 'big': 17, 'small': 18, 'quick': 19
  }
  
  # Reverse mapping for converting IDs back to words
  id_to_word = {v: k for k, v in vocab.items()}
  
  print("Preparing enhanced training data...")
  
  # Enhanced training patterns with more variety
  training_patterns = [
    # Simple completions
    ([vocab['the'], vocab['cat'], vocab['sat'], vocab['on']], vocab['mat']),
    ([vocab['the'], vocab['dog'], vocab['ran']], vocab['fast']),
    ([vocab['the'], vocab['bird'], vocab['flew']], vocab['high']),
    
    # Repeated patterns to strengthen learning
    ([vocab['cat'], vocab['sat'], vocab['on']], vocab['mat']),
    ([vocab['dog'], vocab['ran']], vocab['fast']),
    ([vocab['bird'], vocab['flew']], vocab['high']),
    
    # Additional patterns
    ([vocab['the'], vocab['tree'], vocab['is']], vocab['big']),
    ([vocab['tree'], vocab['is']], vocab['big']),
    ([vocab['the'], vocab['cat'], vocab['is']], vocab['small']),
    ([vocab['cat'], vocab['is']], vocab['small']),
  ]
  
  # Expand training data by creating multiple variations
  training_data = []
  for pattern in training_patterns:
    input_seq, target = pattern
    # Pad sequence to consistent length
    padded_seq = input_seq + [vocab['<PAD>']] * (8 - len(input_seq))
    training_data.append((padded_seq[:8], target))
    
    # Add the pattern multiple times for better learning
    for _ in range(5):  # Repeat each pattern 5 times
      training_data.append((padded_seq[:8], target))
  
  # Training setup with better parameters
  optimizer = torch.optim.Adam(
    list(small_model.parameters()) + list(lm_head.parameters()), 
    lr=0.01,  # Higher learning rate
    weight_decay=1e-5
  )
  criterion = nn.CrossEntropyLoss()
  
  print("Training the model (this may take a moment)...")
  small_model.train()
  lm_head.train()
  
  # Better training loop
  for epoch in range(200):  # More epochs
    total_loss = 0
    
    # Shuffle training data each epoch
    import random
    random.shuffle(training_data)
    
    for input_seq, target in training_data:
      optimizer.zero_grad()
      
      # Convert to tensors
      input_tensor = torch.tensor([input_seq])
      target_tensor = torch.tensor([target])
      
      # Forward pass through transformer
      transformer_output = small_model(input_tensor)
      
      # Get the last non-padding token's representation
      seq_len = sum(1 for token in input_seq if token != vocab['<PAD>'])
      last_token_output = transformer_output[0, seq_len-1, :]
      
      # Predict next word
      logits = lm_head(last_token_output)
      
      # Calculate loss
      loss = criterion(logits.unsqueeze(0), target_tensor)
      loss.backward()
      optimizer.step()
      
      total_loss += loss.item()
    
    if (epoch + 1) % 50 == 0:
      avg_loss = total_loss / len(training_data)
      print(f"Epoch {epoch+1}, Average Loss: {avg_loss:.4f}")
      
      # Early stopping if loss is very low
      if avg_loss < 0.01:
        print(f"Training converged early at epoch {epoch+1}")
        break
  
  print("\nTesting next word prediction...")
  
  # Test the model's predictions
  test_cases = [
    ([vocab['the'], vocab['cat'], vocab['sat'], vocab['on']], "mat"),
    ([vocab['the'], vocab['dog'], vocab['ran']], "fast"),
    ([vocab['the'], vocab['bird'], vocab['flew']], "high"),
    ([vocab['the'], vocab['tree'], vocab['is']], "big"),
    ([vocab['the'], vocab['cat'], vocab['is']], "small")
  ]
  
  small_model.eval()
  lm_head.eval()
  
  correct_predictions = 0
  total_predictions = len(test_cases)
  
  with torch.no_grad():
    for input_tokens, expected_word in test_cases:
      # Pad the input sequence
      padded_input = input_tokens + [vocab['<PAD>']] * (8 - len(input_tokens))
      input_tensor = torch.tensor([padded_input])
      
      # Get transformer output
      transformer_output = small_model(input_tensor)
      
      # Get prediction from the last meaningful token
      last_token_output = transformer_output[0, len(input_tokens)-1, :]
      logits = lm_head(last_token_output)
      
      # Get the predicted word
      predicted_id = int(torch.argmax(logits).item())
      predicted_word = id_to_word.get(predicted_id, '<UNK>')
      
      # Convert input tokens back to words for display
      input_words = [id_to_word[token_id] for token_id in input_tokens if token_id != vocab['<PAD>']]
      
      is_correct = predicted_word == expected_word
      if is_correct:
        correct_predictions += 1
      
      print(f"Input: '{' '.join(input_words)}'")
      print(f"Expected: '{expected_word}' | Predicted: '{predicted_word}'")
      print(f"Confidence: {torch.softmax(logits, dim=0)[predicted_id].item():.3f}")
      print(f"Result: {'✅ CORRECT' if is_correct else '❌ WRONG'}")
      print("-" * 50)
  
  accuracy = (correct_predictions / total_predictions) * 100
  print(f"\n🎯 Overall Accuracy: {accuracy:.1f}% ({correct_predictions}/{total_predictions})")
  
  if accuracy >= 80:
    print("🌟 Excellent! The transformer learned the patterns well!")
  elif accuracy >= 60:
    print("👍 Good! The transformer is learning the patterns.")
  else:
    print("🔄 The model needs more training or different hyperparameters.")
  
  print(f"\n📊 Model Statistics:")
  print(f"   • Parameters: {sum(p.numel() for p in small_model.parameters()) + sum(p.numel() for p in lm_head.parameters()):,}")
  print(f"   • Vocabulary size: {len(vocab)}")
  print(f"   • Training examples: {len(training_data)}")
  
  # Demonstrate the model's internal representations
  print(f"\n🔍 Sample Internal Analysis:")
  print("   Testing how the model processes: 'the cat sat on'")
  
  with torch.no_grad():
    sample_input = [vocab['the'], vocab['cat'], vocab['sat'], vocab['on']] + [vocab['<PAD>']] * 4
    sample_tensor = torch.tensor([sample_input])
    
    # Get embeddings and positional encodings
    embeddings = small_model.embedding(sample_tensor) * math.sqrt(small_model.embedding.embedding_dim)
    pos_encodings = small_model.positional_encoding(embeddings)
    
    print(f"   • Input tokens: {[id_to_word[token] for token in sample_input[:4]]}")
    print(f"   • Embedding dimension: {embeddings.shape[-1]}")
    print(f"   • Sequence length processed: 4 tokens")
    
    # Final prediction
    final_output = small_model(sample_tensor)
    final_logits = lm_head(final_output[0, 3, :])  # Position 3 (after 'on')
    top_3_predictions = torch.topk(final_logits, 3)
    
    print(f"   • Top 3 predictions after 'on':")
    for i, (score, token_id) in enumerate(zip(top_3_predictions.values, top_3_predictions.indices)):
      word = id_to_word[int(token_id.item())]
      confidence = torch.softmax(final_logits, dim=0)[token_id].item()
      print(f"     {i+1}. '{word}' (confidence: {confidence:.3f})")

# Example usage and demonstration
if __name__ == "__main__":
  # Model hyperparameters
  vocab_size = 1000      # Size of vocabulary
  d_model = 512          # Embedding dimension
  num_heads = 8          # Number of attention heads
  num_layers = 6         # Number of encoder layers
  d_ff = 2048           # Feed-forward dimension
  max_seq_length = 100   # Maximum sequence length
  dropout = 0.1          # Dropout rate
  
  # Create the model
  model = SimpleTransformer(vocab_size, d_model, num_heads, num_layers, d_ff, max_seq_length, dropout)
  
  # Example input (batch_size=2, seq_length=10)
  # In practice, these would be token IDs from your vocabulary
  src = torch.randint(0, vocab_size, (2, 10))
  
  # Forward pass
  output = model(src)
  
  print(f"Input shape: {src.shape}")
  print(f"Output shape: {output.shape}")
  print(f"Model has {sum(p.numel() for p in model.parameters())} parameters")
  
  # Example of how you might use this for a classification task
  # Add a classification head
  classifier = nn.Linear(d_model, 2)  # Binary classification
  
  # Use the first token's representation for classification (like BERT's [CLS] token)
  cls_output = classifier(output[:, 0, :])  # Shape: (batch_size, num_classes)
  
  print(f"Classification output shape: {cls_output.shape}")
  
  print("\n" + "="*50)
  print("NEXT WORD PREDICTION TEST")
  print("="*50)
  
  # Test next word prediction capability
  test_next_word_prediction(model, vocab_size, d_model) 