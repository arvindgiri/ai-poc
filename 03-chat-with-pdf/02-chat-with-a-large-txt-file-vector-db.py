#!/usr/bin/env python3
"""
Chat with Large Text File using RAG (Retrieval-Augmented Generation)
This program uses ChromaDB vector database to store embeddings of text chunks
and retrieves relevant context before calling Gemini API for responses.
"""

import json
import requests
import sys
import os
import re
import chromadb
import tiktoken
from sentence_transformers import SentenceTransformer
from typing import List, Dict, Any

class RAGTextFileChat:
    def __init__(self, api_key: str, text_file_path: str):
        """Initialize the RAG chat system with API key and text file path"""
        self.api_key = api_key
        self.text_file_path = text_file_path
        self.base_url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"
        self.embedding_model = SentenceTransformer('all-MiniLM-L6-v2')
        self.chroma_client = chromadb.Client()
        self.tokenizer = tiktoken.get_encoding("cl100k_base")
        self.text_content = self._load_text_file()
        self.text_chunks = self._chunk_text(self.text_content,120,30)
        # Setup vector database
        self._setup_vector_db()
    
    def _load_text_file(self) -> str:
        with open(self.text_file_path, 'r', encoding='utf-8') as file:
            content = file.read().strip()
            print(f"Loaded file {self.text_file_path}. Size: {len(content)} chars")
            return content
    
    def _chunk_text(self, text: str, max_tokens: int = 500, overlap_tokens: int = 50) -> List[Dict[str, Any]]:
        """Split text into overlapping chunks for better context retrieval"""
        print(f"Chunking and embedding started")
        sentences = re.split(r'(?<=[.!?])\s+', text)
        chunks = []
        current_chunk = ""
        current_tokens = 0
        chunk_id = 0
        
        for sentence in sentences:
            sentence_tokens = len(self.tokenizer.encode(sentence))
            if current_tokens + sentence_tokens > max_tokens and current_chunk:
                chunks.append({
                    'id': f"chunk_{chunk_id}",
                    'text': current_chunk.strip(),
                    'tokens': current_tokens
                })
                
                # Start new chunk with overlap from previous chunk
                overlap_text = self._get_overlap_text(current_chunk, overlap_tokens)
                current_chunk = overlap_text + " " + sentence
                current_tokens = len(self.tokenizer.encode(current_chunk))
                chunk_id += 1
            else:
                # Add sentence to current chunk
                current_chunk += " " + sentence if current_chunk else sentence
                current_tokens += sentence_tokens
        
        # Add the final chunk if it contains content
        if current_chunk.strip():
            chunks.append({
                'id': f"chunk_{chunk_id}",
                'text': current_chunk.strip(),
                'tokens': current_tokens
            })
        
        # Print each chunk separately, delimited by 50 asterisks for clarity
        for chunk in chunks:
          print("*" * 50)
          print(chunk)
        print("*" * 50)
        print(f"\n\n ✓ Created {len(chunks)} text chunks")
        return chunks
    
    def _get_overlap_text(self, text: str, overlap_tokens: int) -> str:
        """Get the last portion of text for overlap between chunks"""
        tokens = self.tokenizer.encode(text)
        if len(tokens) <= overlap_tokens:
            return text
        
        overlap_token_list = tokens[-overlap_tokens:]
        overlap_text = self.tokenizer.decode(overlap_token_list)
        return self.tokenizer.decode(overlap_token_list)
    
    def _setup_vector_db(self):
        """Setup ChromaDB collection and store text embeddings"""
        # Delete existing collection if it exists
        try:
            self.chroma_client.delete_collection("text_chunks")
        except:
            pass
        
        # Create new collection
        self.collection = self.chroma_client.create_collection(
            name="text_chunks",
            metadata={"description": "Text chunks from loaded document"}
        )
        
        # Generate embeddings for all chunks
        print("🧠 Generating embeddings for text chunks...")
        chunk_texts = [chunk['text'] for chunk in self.text_chunks]
        chunk_ids = [chunk['id'] for chunk in self.text_chunks]
        
        # Generate embeddings using sentence transformer
        embeddings = self.embedding_model.encode(chunk_texts).tolist()

        print("🔍 Adding embeddings in vector database...")
        # Store in ChromaDB
        self.collection.add(
            embeddings=embeddings,
            documents=chunk_texts,
            ids=chunk_ids
        )
        
        print(f"✓ Stored {len(self.text_chunks)} chunks in vector database")
    
    def _retrieve_relevant_context(self, query: str, top_k: int = 3) -> str:
        """Retrieve most relevant text chunks for the given query"""
        print("_retrieve_relevant_context started")
        # Generate embedding for the query
        query_embedding = self.embedding_model.encode([query]).tolist()
        
        # Search for similar chunks
        results = self.collection.query(
            query_embeddings=query_embedding,
            n_results=top_k
        )
        
        # Combine retrieved documents
        if results['documents'] and results['documents'][0]:
            retrieved_texts = results['documents'][0]
            context = "\n\n---\n\n".join(retrieved_texts)
            
            print(f"🔍 Retrieved {len(retrieved_texts)} relevant text chunks")
            return context
        else:
            print("⚠️  No relevant context found")
            return ""
    
    def _create_rag_prompt(self, topic: str, context: str) -> str:
        """Create a RAG prompt using retrieved context and user topic"""
        prompt = f"""CONTEXT: Below is relevant information retrieved from "The Power of Now" and "Stillness Speaks" by Eckhart Tolle:

{context}

TASK: Based EXCLUSIVELY on the above context, generate a focused and insightful response (150-200 words) about the topic: "{topic}"

RULES:
1. ONLY use information explicitly present in the provided context above
2. If the topic is not adequately covered in the context, state that clearly
3. Reference specific concepts and insights from the context when explaining the topic
4. Maintain the contemplative and practical tone of Tolle's teachings
5. Make the response personally applicable and actionable
6. Do not add any external knowledge beyond what's provided in the context

Focus on providing practical wisdom that can be applied immediately in daily life."""
        
        return prompt
    
    def _call_gemini_api(self, prompt: str) -> str:
        """Make API call to Gemini and return the response"""
        headers = {
            'Content-Type': 'application/json',
            'X-goog-api-key': self.api_key
        }
        
        payload = {
            "contents": [
                {
                    "parts": [
                        {
                            "text": prompt
                        }
                    ]
                }
            ],
            "generationConfig": {
                "temperature": 0.7,
                "topK": 40,
                "topP": 0.95,
                "maxOutputTokens": 300
            }
        }
        
        try:
            # Make API request
            response = requests.post(self.base_url, headers=headers, json=payload, timeout=30)
            response.raise_for_status()
            
            # Parse response
            response_data = response.json()
            
            # Extract generated text
            if 'candidates' in response_data and len(response_data['candidates']) > 0:
                candidate = response_data['candidates'][0]
                if 'content' in candidate and 'parts' in candidate['content']:
                    return candidate['content']['parts'][0]['text']
            return "❌ No valid response generated"
            
        except requests.exceptions.RequestException as e:
            return f"❌ API request failed: {e}"
        except json.JSONDecodeError as e:
            return f"❌ Failed to parse API response: {e}"
        except KeyError as e:
            return f"❌ Unexpected response format: {e}"
        except Exception as e:
            return f"❌ Unexpected error: {e}"
    
    def chat_loop(self):
        """Main chat loop for user interaction with RAG functionality"""
        print("chat_loop started")
        print("=" * 70)
        print(f"🧘 RAG-Enhanced Chat with {self.text_file_path}")
        print("=" * 70)
        print("Ask about any topic and get insights based on the most relevant")
        print(f"passages from {self.text_file_path}")
        print("Type 'quit', 'exit', or 'bye' to end the session.")
        print("Type 'stats' to see database statistics.\n")
        
        while True:
            try:
                # Get user input
                user_topic = input("💭 Enter a topic to explore: ").strip()
                
                # Check for exit commands
                if user_topic.lower() in ['quit', 'exit', 'bye', 'q']:
                    print("\n🙏 Thank you for exploring mindfulness and presence. Stay present!")
                    break
                
                # Check for stats command
                if user_topic.lower() == 'stats':
                    self._show_stats()
                    continue
                
                # Skip empty input
                if not user_topic:
                    print("⚠️  Please enter a topic to explore.\n")
                    continue
                
                print(f"\n🔍 Searching for relevant content about '{user_topic}'...")
                
                # Retrieve relevant context using RAG
                relevant_context = self._retrieve_relevant_context(user_topic, top_k=3)
                
                print(f"\n#################### Relevant content (from Vector DB) ####################################")
                print(f"\n🔍  '{relevant_context}'")


                print(f"\n######################################################################################")

                if not relevant_context:
                    print("❌ No relevant content found for this topic.")
                    continue
                
                print("🤔 Generating insights from retrieved content...")
                
                # Create RAG prompt and get response
                rag_prompt = self._create_rag_prompt(user_topic, relevant_context)
                response = self._call_gemini_api(rag_prompt)
                
                # Display response
                print("\n" + "─" * 60)
                print("📝 Response:")
                print("─" * 60)
                print(response)
                print("─" * 60 + "\n")
                
            except KeyboardInterrupt:
                print("\n\n🙏 Session ended")
                break
            except Exception as e:
                print(f"❌ Unexpected error: {e}\n")
    
    def _show_stats(self):
        """Display statistics about the vector database"""
        print("_show_stats started")
        print("\n📊 Vector Database Statistics:")
        print("─" * 40)
        print(f"Total chunks stored: {len(self.text_chunks)}")
        print(f"Average chunk size: {sum(chunk['tokens'] for chunk in self.text_chunks) // len(self.text_chunks)} tokens")
        print(f"Embedding model: all-MiniLM-L6-v2")
        print(f"Vector database: ChromaDB")
        print("─" * 40 + "\n")

def main():
    # Gemini API Key
    API_KEY = "AIzaSyAHoMnMvscxaZ4F0kZ2O2oHKarj4h1R90U"
    
    # Get the directory where this script is located
    script_dir = os.path.dirname(os.path.abspath(__file__))
    TEXT_FILE_PATH = os.path.join(script_dir, "thirsty-crow.txt")
    
    print("🚀 Initializing RAG-Enhanced Chat System...")
    print("This may take a few moments on first run...\n")
    
    chat = RAGTextFileChat(API_KEY, TEXT_FILE_PATH)
    chat.chat_loop()

if __name__ == "__main__":
    main()
