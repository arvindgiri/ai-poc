#!/usr/bin/env python3
"""
Simple LLM implementation for processing text and answering queries.
Uses TF-IDF (Term Frequency-Inverse Document Frequency) for better text ranking.
"""

import re
import math
from collections import Counter
from typing import List, Dict, Tuple


class SimpleLLM:
  """A basic LLM implementation using TF-IDF scoring for text analysis."""
  
  def __init__(self):
    print("LLM init called")
    """Initialize the SimpleLLM."""
    self.knowledge_base = ""  # Store the preprocessed text
    self.sentences = []  # List of individual sentences
    self.keywords = {}  # Dictionary of keywords with frequencies
    self.vocabulary = set()  # All unique terms across all sentences
    self.tf_idf_matrix = []  # TF-IDF vectors for each sentence
    self.idf_scores = {}  # Pre-computed IDF scores for all terms
  
  def preprocess_text(self, text: str) -> str:
    """Clean and preprocess text."""
    print("LLM preprocess_text called")
    # Remove extra whitespace and normalize
    outtext = re.sub(r'\s+', ' ', text.strip())  # Replace multiple spaces with single space
    return outtext
  
  def extract_sentences(self, text: str) -> List[str]:
    """Split text into sentences."""
    # Simple sentence splitting using punctuation
    sentences = re.split(r'[.!?]+', text)
    return [s.strip() for s in sentences if s.strip()]  # Remove empty sentences
  
  def tokenize(self, text: str) -> List[str]:
    """Tokenize text into words, removing stop words."""
    # Convert to lowercase and extract words
    words = re.findall(r'\b[a-zA-Z]+\b', text.lower())
    
    # Filter out common stop words
    stop_words = {
      'the', 'a', 'an', 'and', 'or', 'but', 'in', 'on', 'at', 'to', 'for',
      'of', 'with', 'by', 'is', 'are', 'was', 'were', 'be', 'been', 'being',
      'have', 'has', 'had', 'do', 'does', 'did', 'will', 'would', 'could',
      'should', 'may', 'might', 'can', 'this', 'that', 'these', 'those'
    }
    
    # Keep only meaningful words (not stop words and length > 2)
    filtered_words = [word for word in words if word not in stop_words and len(word) > 2]
    return filtered_words
  
  def extract_keywords(self, text: str) -> Dict[str, int]:
    """Extract keywords from text with frequency."""
    words = self.tokenize(text)
    return dict(Counter(words))  # Return word frequency dictionary
  
  def calculate_tf(self, term: str, document: List[str]) -> float:
    """Calculate Term Frequency (TF) for a term in a document."""
    if not document:  # Handle empty document
      return 0.0
    term_count = document.count(term)
    return term_count / len(document)
  
  def calculate_idf(self, term: str, corpus: List[List[str]]) -> float:
    """Calculate Inverse Document Frequency (IDF) for a term across corpus."""
    if not corpus:  # Handle empty corpus
      return 0.0
    
    # Count documents containing the term
    docs_containing_term = sum(1 for doc in corpus if term in doc)
    
    if docs_containing_term == 0:  # Term not found in any document
      return 0.0
    
    # Calculate IDF using log formula
    return math.log(len(corpus) / docs_containing_term)
  
  def build_tf_idf_matrix(self, tokenized_sentences: List[List[str]]):
    """Build TF-IDF matrix for all sentences."""
    print("Building TF-IDF matrix...")
    
    # Build vocabulary from all sentences
    self.vocabulary = set()
    for sentence_tokens in tokenized_sentences:
      self.vocabulary.update(sentence_tokens)
    
    # Pre-compute IDF scores for all terms
    self.idf_scores = {}
    for term in self.vocabulary:
      self.idf_scores[term] = self.calculate_idf(term, tokenized_sentences)
    
    # Build TF-IDF vectors for each sentence
    self.tf_idf_matrix = []
    for sentence_tokens in tokenized_sentences:
      tf_idf_vector = {}
      for term in self.vocabulary:
        tf = self.calculate_tf(term, sentence_tokens)
        idf = self.idf_scores[term]
        tf_idf_vector[term] = tf * idf
      self.tf_idf_matrix.append(tf_idf_vector)
    
    print(f"Built TF-IDF matrix with {len(self.vocabulary)} unique terms.")
  
  def calculate_cosine_similarity(self, query_vector: Dict[str, float], 
                                 sentence_vector: Dict[str, float]) -> float:
    """Calculate cosine similarity between query and sentence vectors."""
    # Calculate dot product
    dot_product = 0.0
    for term in query_vector:
      if term in sentence_vector:
        dot_product += query_vector[term] * sentence_vector[term]
    
    # Calculate magnitudes
    query_magnitude = math.sqrt(sum(score ** 2 for score in query_vector.values()))
    sentence_magnitude = math.sqrt(sum(score ** 2 for score in sentence_vector.values()))
    
    # Avoid division by zero
    if query_magnitude == 0 or sentence_magnitude == 0:
      return 0.0
    
    return dot_product / (query_magnitude * sentence_magnitude)
  
  def learn(self, text: str):
    """Process and learn from input text."""
    print("LLM learn called")
    print("Preprocessing text and creating knowledge base")
    
    # Preprocess the input text
    self.knowledge_base = self.preprocess_text(text)
    
    # Extract individual sentences
    self.sentences = self.extract_sentences(self.knowledge_base)
    
    # Tokenize all sentences
    tokenized_sentences = [self.tokenize(sentence) for sentence in self.sentences]
    
    # Build TF-IDF matrix
    self.build_tf_idf_matrix(tokenized_sentences)
    
    # Extract keywords with frequencies (for backward compatibility)
    self.keywords = self.extract_keywords(self.knowledge_base)
    
    print(f"Learned from text with {len(self.sentences)} sentences and {len(self.keywords)} unique keywords.")
  
  def calculate_relevance(self, query: str, sentence_idx: int) -> float:
    """Calculate TF-IDF-based relevance score between query and sentence."""
    if sentence_idx >= len(self.tf_idf_matrix):
      return 0.0
    
    # Tokenize query
    query_tokens = self.tokenize(query)
    if not query_tokens:
      return 0.0
    
    # Build TF-IDF vector for query
    query_vector = {}
    query_term_counts = Counter(query_tokens)
    
    for term in query_term_counts:
      if term in self.vocabulary:
        # Calculate TF for query term
        tf = query_term_counts[term] / len(query_tokens)
        # Use pre-computed IDF
        idf = self.idf_scores.get(term, 0.0)
        query_vector[term] = tf * idf
    
    # Get sentence TF-IDF vector
    sentence_vector = self.tf_idf_matrix[sentence_idx]
    
    # Calculate cosine similarity
    return self.calculate_cosine_similarity(query_vector, sentence_vector)
  
  def find_best_answer(self, query: str) -> str:
    """Find the best answer for a given query using TF-IDF scoring."""
    if not self.sentences:  # Check if we have any knowledge
      return "I don't have any knowledge to answer your question."
    
    # Calculate TF-IDF relevance scores for all sentences
    scored_sentences = []
    for i, sentence in enumerate(self.sentences):
      score = self.calculate_relevance(query, i)  # Get TF-IDF relevance score
      scored_sentences.append((sentence, score))  # Store sentence with score
    
    # Sort sentences by relevance score (highest first)
    scored_sentences.sort(key=lambda x: x[1], reverse=True)
    
    # Return the most relevant sentence if score is above threshold
    if scored_sentences[0][1] > 0.05:  # Lower threshold for TF-IDF scores
      return scored_sentences[0][0]  # Return best sentence
    else:
      # Return best guess with low confidence warning
      return "I am not confident but here is my guess: " + scored_sentences[0][0]
  
  def query(self, question: str) -> str:
    """Answer a query based on learned knowledge using TF-IDF."""
    print(f"\nQuery: {question}")
    answer = self.find_best_answer(question)  # Find best matching sentence
    print(f"Answer: {answer}")
    return answer


def main():
  """Main function demonstrating the TF-IDF enhanced SimpleLLM."""
  # Create LLM instance
  llm = SimpleLLM()
  
  # Paragraph about Vipassana meditation
  patanjali_text = """
  Vipassana, meaning "insight" or "clear seeing," is one of the oldest forms of meditation, said to have been taught by the Buddha himself over 2,500 years ago. It is a practice of self-observation and awareness, designed to purify the mind by understanding the true nature of reality. Unlike concentration-based techniques that focus on a single object, Vipassana involves observing bodily sensations, thoughts, and emotions as they arise, without attachment or aversion.
  The technique begins with Anapana, or observation of the natural breath, which helps calm and sharpen the mind. Once the mind is sufficiently focused, practitioners begin scanning their body in a systematic way to observe physical sensations. These sensations — whether pleasant, unpleasant, or neutral — are not reacted to. Instead, one simply notes them and maintains equanimity. Over time, this practice reveals the impermanence (anicca) of all things, leading to deep insight into the nature of suffering (dukkha) and the illusion of a fixed self (anatta).
  Vipassana is typically taught in silent 10-day retreats, where participants follow a strict code of discipline, including noble silence, abstention from distractions, and several hours of meditation daily. The goal is not relaxation but transformation — to uproot deep-seated mental conditioning and reactiveness.
  The benefits of Vipassana, reported by many practitioners, include greater emotional resilience, clarity of thought, reduced anxiety, and a more compassionate outlook on life. However, it is a demanding path, requiring patience, commitment, and the willingness to face one's inner turmoil.
  """
  
  print("=== Simple LLM Demo ===")
  print("=== LLM is learning from the text ===")
  
  # Learn from the text
  llm.learn(patanjali_text)
  
  # Ask various queries
  queries = [
    "What does the word Vipassana mean?",
    "How does Vipassana differ from concentration-based meditation?",
    "What is the role of Anapana in Vipassana?",
    "What are the three characteristics of existence that Vipassana reveals?",
    "Why are Vipassana retreats conducted in silence?",
    "What are some benefits reported by those who practice Vipassana?"
  ]
  
  print("\n=== Queries and Answers ===")
  for query in queries:
    llm.query(query)  # Process each query
    print()  # Add blank line between queries


if __name__ == "__main__":
  main()  # Run the demo 