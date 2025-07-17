from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity

# Initialize the model once, as it's a heavy object
model = SentenceTransformer('all-MiniLM-L6-v2')

def main():
    """Main function that calls the similarity calculations"""
    # --- Calling the method with different arguments ---

    # 1. Comparing two similar sentences
    calculate_and_print_similarity("A boy is playing football in the field.", "A kid is kicking a ball on the grass.")

    # 2. Comparing two somewhat related sentences
    calculate_and_print_similarity("The weather is nice and sunny today.", "I am going to the beach for a swim.")

    # 3. Comparing two unrelated sentences
    calculate_and_print_similarity("I like to program in Python.", "The best pizza has pineapple on it.")

    # 4. Comparing two single words that are related
    calculate_and_print_similarity("king", "queen")
    
    print("\n--- Testing for LOW/NEGATIVE cosine similarities ---")
    
    # 5. Testing opposite emotions
    calculate_and_print_similarity("I am extremely happy and joyful today!", "I am deeply sad and depressed today.")
    
    # 6. Testing opposite actions
    calculate_and_print_similarity("I love this movie, it's absolutely amazing!", "I hate this movie, it's completely terrible!")
    
    # 7. Testing opposite concepts
    calculate_and_print_similarity("The light is very bright and illuminating.", "The darkness is completely black and void.")
    
    # 8. Testing opposite directions/movements
    calculate_and_print_similarity("Go up and climb higher to the top.", "Go down and fall lower to the bottom.")
    
    # 9. Testing opposite temperatures
    calculate_and_print_similarity("It's extremely hot and burning outside.", "It's freezing cold and icy outside.")
    
    # 10. Testing positive vs negative statements
    calculate_and_print_similarity("Yes, I completely agree with everything you said.", "No, I completely disagree with everything you said.")
    
    print("\n--- Testing for EXTREMELY LOW similarities (closest to -1) ---")
    
    # 11. Technical vs emotional
    calculate_and_print_similarity("The TCP/IP protocol stack implements network communication layers.", "My heart feels broken and I'm crying tears of sorrow.")
    
    # 12. Mathematical vs poetic
    calculate_and_print_similarity("f(x) = 2x + 3 is a linear function with slope 2", "The moonbeams dance gracefully upon the ocean waves tonight")
    
    # 13. Medical vs cooking
    calculate_and_print_similarity("The patient exhibits acute myocardial infarction symptoms", "Add two tablespoons of vanilla extract to the chocolate cake batter")
    
    # 14. Programming vs nature
    calculate_and_print_similarity("import pandas as pd; df.groupby('column').agg({'value': 'sum'})", "The gentle breeze whispers through the ancient oak tree leaves")
    
    # 15. Legal vs sports
    calculate_and_print_similarity("The defendant pleads not guilty to charges of contract violation", "The goalkeeper made an amazing save during the penalty kick")


def calculate_and_print_similarity(text1: str, text2: str):
    """
    Encodes two text strings and prints their cosine similarity.
    
    Args:
        text1: The first string to compare.
        text2: The second string to compare.
    """
    # The model.encode() method creates a vector embedding for each string
    embedding1 = model.encode(text1)
    embedding2 = model.encode(text2)

    # We reshape the 1D embeddings into 2D arrays for compatibility with cosine_similarity
    similarity = cosine_similarity([embedding1], [embedding2])
    
    print(f"Similarity between '{text1}' and '{text2}': {similarity[0][0]:.4f}")

# This ensures main() only runs when the script is executed directly
if __name__ == "__main__":
    main()






