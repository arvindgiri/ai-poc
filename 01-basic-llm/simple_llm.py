#!/usr/bin/env python3
"""
Simple LLM implementation for processing text and answering queries.
Uses basic keyword matching and text similarity for responses.
"""

import re
from collections import Counter
from typing import List, Dict, Tuple


class SimpleLLM:
  """A basic LLM implementation using keyword matching and text analysis."""
  
  def __init__(self):
    print ("LLM init called")
    """Initialize the SimpleLLM."""
    self.knowledge_base = ""
    self.sentences = []
    self.keywords = {}
  
  def preprocess_text(self, text: str) -> str:
    """Clean and preprocess text."""
    # Remove extra whitespace and normalize
    print ("LLM preprocess_text called")
    outtext = re.sub(r'\s+', ' ', text.strip())
    print (f"LLM preprocess_text input text:\n {text}\n output text: {outtext}")
    return outtext
  
  def extract_sentences(self, text: str) -> List[str]:
    """Split text into sentences."""
    # Simple sentence splitting
    sentences = re.split(r'[.!?]+', text)
    return [s.strip() for s in sentences if s.strip()]
  
  def extract_keywords(self, text: str) -> Dict[str, int]:
    """Extract keywords from text with frequency."""
    # Convert to lowercase and remove punctuation
    words = re.findall(r'\b[a-zA-Z]+\b', text.lower())
    
    # Filter out common stop words
    stop_words = {
      'the', 'a', 'an', 'and', 'or', 'but', 'in', 'on', 'at', 'to', 'for',
      'of', 'with', 'by', 'is', 'are', 'was', 'were', 'be', 'been', 'being',
      'have', 'has', 'had', 'do', 'does', 'did', 'will', 'would', 'could',
      'should', 'may', 'might', 'can', 'this', 'that', 'these', 'those'
    }
    
    filtered_words = [word for word in words if word not in stop_words and len(word) > 2]
    return dict(Counter(filtered_words))
  
  def learn(self, text: str):
    """Process and learn from input text."""
    self.knowledge_base = self.preprocess_text(text)
    self.sentences = self.extract_sentences(self.knowledge_base)
    self.keywords = self.extract_keywords(self.knowledge_base)
    print(f"Learned from text with {len(self.sentences)} sentences and {len(self.keywords)} unique keywords.")
  
  def calculate_relevance(self, query: str, sentence: str) -> float:
    """Calculate relevance score between query and sentence."""
    query_words = set(re.findall(r'\b[a-zA-Z]+\b', query.lower()))
    sentence_words = set(re.findall(r'\b[a-zA-Z]+\b', sentence.lower()))
    
    if not query_words or not sentence_words:
      return 0.0
    
    # Calculate Jaccard similarity
    intersection = len(query_words.intersection(sentence_words))
    union = len(query_words.union(sentence_words))
    
    return intersection / union if union > 0 else 0.0
  
  def find_best_answer(self, query: str) -> str:
    """Find the best answer for a given query."""
    if not self.sentences:
      return "I don't have any knowledge to answer your question."
    
    # Calculate relevance scores for all sentences
    scored_sentences = []
    for sentence in self.sentences:
      score = self.calculate_relevance(query, sentence)
      scored_sentences.append((sentence, score))
    
    # Sort by relevance score
    scored_sentences.sort(key=lambda x: x[1], reverse=True)
    
    # Return the most relevant sentence if score is above threshold
    if scored_sentences[0][1] > 0.1:
      return scored_sentences[0][0]
    else:
      return "I couldn't find a relevant answer in my knowledge base."
  
  def query(self, question: str) -> str:
    """Answer a query based on learned knowledge."""
    print(f"\nQuery: {question}")
    answer = self.find_best_answer(question)
    print(f"Answer: {answer}")
    return answer


def main():
  """Main function demonstrating the SimpleLLM."""
  # Create LLM instance
  llm = SimpleLLM()
  
  # Paragraph about Patanjali meditation
  patanjali_text = """
  Vipassana, meaning "insight" or "clear seeing," is one of the oldest forms of meditation, said to have been taught by the Buddha himself over 2,500 years ago. It is a practice of self-observation and awareness, designed to purify the mind by understanding the true nature of reality. Unlike concentration-based techniques that focus on a single object, Vipassana involves observing bodily sensations, thoughts, and emotions as they arise, without attachment or aversion.
  The technique begins with Anapana, or observation of the natural breath, which helps calm and sharpen the mind. Once the mind is sufficiently focused, practitioners begin scanning their body in a systematic way to observe physical sensations. These sensations — whether pleasant, unpleasant, or neutral — are not reacted to. Instead, one simply notes them and maintains equanimity. Over time, this practice reveals the impermanence (anicca) of all things, leading to deep insight into the nature of suffering (dukkha) and the illusion of a fixed self (anatta).
  Vipassana is typically taught in silent 10-day retreats, where participants follow a strict code of discipline, including noble silence, abstention from distractions, and several hours of meditation daily. The goal is not relaxation but transformation — to uproot deep-seated mental conditioning and reactiveness.
  The benefits of Vipassana, reported by many practitioners, include greater emotional resilience, clarity of thought, reduced anxiety, and a more compassionate outlook on life. However, it is a demanding path, requiring patience, commitment, and the willingness to face one’s inner turmoil.
  """
  
  print("=== Simple LLM Demo ===")
  print(f"Input text: {patanjali_text.strip()}")
  
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
    llm.query(query)
    print()


if __name__ == "__main__":
  main() 