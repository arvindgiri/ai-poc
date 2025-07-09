# Simple LLM with TF-IDF Scoring

This implementation enhances the basic LLM by using **TF-IDF (Term Frequency-Inverse Document Frequency)** scoring for better text similarity and relevance ranking.

## What is TF-IDF?

TF-IDF is a numerical statistic that reflects how important a word is to a document in a collection of documents. It consists of two components:

- **Term Frequency (TF)**: How frequently a term appears in a document
- **Inverse Document Frequency (IDF)**: How rare or common a term is across all documents

### Mathematical Formula

- **TF(term, document)** = count(term in document) / total_words_in_document
- **IDF(term, corpus)** = log(total_documents / documents_containing_term)
- **TF-IDF(term, document, corpus)** = TF(term, document) × IDF(term, corpus)

## Key Improvements over Basic Token Matching

1. **Better Relevance Scoring**: TF-IDF considers both term frequency and rarity, giving higher scores to terms that are frequent in a document but rare across the corpus.

2. **Cosine Similarity**: Uses cosine similarity between TF-IDF vectors instead of simple Jaccard similarity, providing more nuanced similarity measurements.

3. **Vocabulary Management**: Builds a comprehensive vocabulary from all sentences for consistent vector representation.

4. **Vector-based Approach**: Represents both queries and sentences as TF-IDF vectors, enabling more sophisticated similarity calculations.

## Features

- **Text Preprocessing**: Cleans and normalizes input text
- **Sentence Extraction**: Splits text into individual sentences
- **Stop Word Filtering**: Removes common words that don't contribute to meaning
- **TF-IDF Vectorization**: Converts text into TF-IDF vectors
- **Cosine Similarity**: Calculates similarity between query and sentence vectors
- **Relevance Ranking**: Ranks sentences by TF-IDF-based relevance scores

## How It Works

1. **Learning Phase**:
   - Preprocesses input text
   - Extracts sentences
   - Tokenizes all sentences
   - Builds vocabulary from all terms
   - Calculates TF-IDF vectors for each sentence

2. **Query Phase**:
   - Tokenizes the query
   - Calculates TF-IDF vector for the query
   - Computes cosine similarity with all sentence vectors
   - Returns the most relevant sentence

## Usage

```python
# Create LLM instance
llm = SimpleLLM()

# Learn from text
text = "Your knowledge base text here..."
llm.learn(text)

# Query the LLM
answer = llm.query("What is your question?")
print(answer)
```

## Example Output

```
=== Simple LLM Demo with TF-IDF Scoring ===
=== LLM is learning from the text ===
Building TF-IDF vectors...
Built TF-IDF vectors with vocabulary size: 89
Learned from text with 4 sentences and 89 unique keywords.

=== Queries and Answers ===
Query: What does the word Vipassana mean?
Answer: Vipassana, meaning "insight" or "clear seeing," is one of the oldest forms of meditation...
```

## Advantages of TF-IDF Approach

1. **Context Awareness**: Considers the importance of terms across the entire corpus
2. **Rare Term Emphasis**: Gives higher weight to unique, informative terms
3. **Noise Reduction**: Reduces impact of common words through IDF weighting
4. **Scalability**: Works well with larger text collections
5. **Mathematical Foundation**: Based on established information retrieval principles

## Requirements

- Python 3.6+
- No external dependencies (uses only standard library)

## Running the Demo

```bash
python simple_llm.py
```

This will demonstrate the TF-IDF-based LLM answering questions about Vipassana meditation, showing improved relevance scoring compared to basic token matching approaches. 