#!/usr/bin/env python3
"""
Chat with Text File using Gemini API
This program reads content from pon.txt and generates responses based on user topics
using Google's Gemini API.
"""

import json
import requests
import sys
import os

class TextFileChat:
    def __init__(self, api_key, text_file_path):
        """Initialize the chat system with API key and text file path"""
        self.api_key = api_key
        self.text_file_path = text_file_path
        self.base_url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"
        self.text_content = self._load_text_file()
    
    def _load_text_file(self):
        """Load and return content from the text file"""
        try:
            with open(self.text_file_path, 'r', encoding='utf-8') as file:
                content = file.read().strip()
                print(f"✓ Successfully loaded text file: {self.text_file_path}")
                print(f"  Content length: {len(content)} characters\n")
                return content
        except FileNotFoundError:
            print(f"❌ Error: Text file '{self.text_file_path}' not found")
            sys.exit(1)
        except Exception as e:
            print(f"❌ Error reading file: {e}")
            sys.exit(1)
    
    def _create_prompt(self, topic):
        """Create a contextual prompt using the loaded text and user topic"""
        prompt = f"""IMPORTANT: You must ONLY use information from the provided text content below. Do not add any external knowledge or information not present in this text.

TEXT CONTENT:
{self.text_content}

TASK: Based EXCLUSIVELY on the above text content, generate a focused paragraph (100-150 words) about the topic: "{topic}"

RULES:
1. ONLY use concepts, quotes, and insights that are explicitly mentioned in the provided text
2. If the topic is not covered in the provided text, clearly state that the information is not available in the source material
3. Do not add any information from your general knowledge about Eckhart Tolle or these books
4. Reference specific concepts from the text when explaining the topic
5. Stay strictly within the boundaries of the provided content"""
        
        return prompt
    
    def _call_gemini_api(self, prompt):
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
            ]
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
        """Main chat loop for user interaction"""
        print("=" * 60)
        print("🧘 Chat with 'The Power of Now' and 'Stillness Speaks'")
        print("=" * 60)
        print("Ask about any topic and get insights based on Eckhart Tolle's teachings!")
        print("Type 'quit', 'exit', or 'bye' to end the session.\n")
        
        while True:
            try:
                # Get user input
                user_topic = input("💭 Enter a topic to explore: ").strip()
                
                # Check for exit commands
                if user_topic.lower() in ['quit', 'exit', 'bye', 'q']:
                    print("\n🙏 Thank you for exploring mindfulness and presence. Stay present!")
                    break
                
                # Skip empty input
                if not user_topic:
                    print("⚠️  Please enter a topic to explore.\n")
                    continue
                
                print(f"\n🤔 Generating insights about '{user_topic}'...")
                
                # Create prompt and get response
                prompt = self._create_prompt(user_topic)
                response = self._call_gemini_api(prompt)
                
                # Display response
                print("\n" + "─" * 50)
                print("📝 Response:")
                print("─" * 50)
                print(response)
                print("─" * 50 + "\n")
                
            except KeyboardInterrupt:
                print("\n\n🙏 Session ended. Stay mindful!")
                break
            except Exception as e:
                print(f"❌ Unexpected error: {e}\n")

def main():
    """Main function to run the chat application"""
    # Configuration
    API_KEY = "AIzaSyAHoMnMvscxaZ4F0kZ2O2oHKarj4h1R90U"
    # Get the directory where this script is located
    script_dir = os.path.dirname(os.path.abspath(__file__))
    TEXT_FILE_PATH = os.path.join(script_dir, "pon.txt")
    
    # Validate API key
    if not API_KEY:
        print("❌ Error: Gemini API key is required")
        sys.exit(1)
    
    # Create and run chat instance
    try:
        chat = TextFileChat(API_KEY, TEXT_FILE_PATH)
        chat.chat_loop()
    except Exception as e:
        print(f"❌ Failed to initialize chat system: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
