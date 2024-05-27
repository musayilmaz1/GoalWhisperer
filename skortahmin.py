# -*- coding: utf-8 -*-
"""bitirme2.ipynb

Automatically generated by Colab.

Original file is located at
    https://colab.research.google.com/drive/1x9hUW7ZUSH6RLFwQFUCD-GyyVOaxf2OM
"""

from google.colab import drive
drive.mount('/content/drive')

import pandas as pd
import numpy as np

# Örnek olarak, CSV dosyası okuma
file_path = '/content/drive/MyDrive/Colab Notebooks/tsl_dataset.csv'
df = pd.read_csv(file_path)

df.head()

print(df.columns)

# Yeni bir değişken oluştur
df_cleaned = df.copy()
#kullanilacak kategorik veriler
df_cleaned = df_cleaned[['Season', 'home', 'visitor', 'FT', 'hgoal', 'vgoal', 'totgoal', 'goaldiff', 'result', 'HT', 'hgoal_half','vgoal_half','half_totgoal','half_goaldiff','result_half',]]

#yeni olusan kategorik veriler olan veri seti
print(df_cleaned)
print(df_cleaned.columns)

new_column_names = {
    'Season': 'Sezon',
    'home': 'Ev Sahibi',
    'visitor': 'Misafir',
    'FT': 'Maç Sonucu',
    'hgoal': 'Ev Sahibi Gol',
    'vgoal': 'Misafir Gol',
    'totgoal': 'Toplam Gol',
    'goaldiff': 'Gol Farkı',
    'result': 'Sonuç',
    'HT': 'İlk Yarı',
    'hgoal_half': 'Ev Sahibi İlk Yarı Gol',
    'vgoal_half': 'Misafir İlk Yarı Gol',
    'half_totgoal': 'İlk Yarı Toplam Gol',
    'half_goaldiff': 'İlk Yarı Gol Farkı',
    'result_half': 'İlk Yarı Sonuç'
}
df_cleaned = df_cleaned.rename(columns=new_column_names)
print(df_cleaned)
print(df_cleaned.columns)

# Boş değer kontrolü
print(df_cleaned.isnull().sum())

import pandas as pd
from sklearn.preprocessing import LabelEncoder



# Kategorik verileri sayısal verilere dönüştürme
le_home = LabelEncoder()
le_visitor = LabelEncoder()

df_cleaned['Ev Sahibi'] = le_home.fit_transform(df_cleaned['Ev Sahibi'])
df_cleaned['Misafir'] = le_visitor.fit_transform(df_cleaned['Misafir'])

# Dönüştürülmüş veri setini inceleme
print(df_cleaned.head())

df_cleaned.head(30)

le_result = LabelEncoder()
df_cleaned['Sonuç'] = le_result.fit_transform(df_cleaned['Sonuç'])
df_cleaned['İlk Yarı Sonuç'] = le_result.fit_transform(df_cleaned['İlk Yarı Sonuç'])
df_cleaned.head(30)

print(df_cleaned.columns)

import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, confusion_matrix, classification_report
X = df_cleaned.drop(columns=['Maç Sonucu', 'Sonuç', 'İlk Yarı', 'İlk Yarı Sonuç'])
y_full_match = df_cleaned['Sonuç']
y_half_time = df_cleaned['İlk Yarı Sonuç']
# Verileri eğitim ve test setlerine bölelim
X_train_full, X_test_full, y_train_full, y_test_full = train_test_split(X, y_full_match, test_size=0.2, random_state=42)
X_train_half, X_test_half, y_train_half, y_test_half = train_test_split(X, y_half_time, test_size=0.2, random_state=42)

# StandardScaler objesi oluştur
scaler = StandardScaler()

# Verileri fit_transform ile dönüştür
X_train_full = scaler.fit_transform(X_train_full)
X_test_full = scaler.transform(X_test_full)

X_train_half = scaler.fit_transform(X_train_half)
X_test_half = scaler.transform(X_test_half)

# Model oluşturma ve eğitim
rf_full_match = RandomForestClassifier(random_state=42)
rf_half_time = RandomForestClassifier(random_state=42)

rf_full_match.fit(X_train_full, y_train_full)
rf_half_time.fit(X_train_half, y_train_half)

# Verileri fit_transform ile dönüştür
X_train_full = scaler.fit_transform(X_train_full)
X_test_full = scaler.transform(X_test_full)

X_train_half = scaler.fit_transform(X_train_half)
X_test_half = scaler.transform(X_test_half)

# Model oluşturma ve eğitim
rf_full_match = RandomForestClassifier(random_state=42)
rf_half_time = RandomForestClassifier(random_state=42)

rf_full_match.fit(X_train_full, y_train_full)
rf_half_time.fit(X_train_half, y_train_half)

# Tahminler
y_pred_full = rf_full_match.predict(X_test_full)
y_pred_half = rf_half_time.predict(X_test_half)

# Accuracy değerleri
accuracy_full = accuracy_score(y_test_full, y_pred_full)
accuracy_half = accuracy_score(y_test_half, y_pred_half)

print("Tam Maç Sonucu Tahmini Doğruluk Oranı:", accuracy_full)
print("İlk Yarı Sonucu Tahmini Doğruluk Oranı:", accuracy_half)

# Sınıflandırma raporu
print("\nTam Maç Sonucu Sınıflandırma Raporu:")
print(classification_report(y_test_full, y_pred_full))

print("\nİlk Yarı Sonucu Sınıflandırma Raporu:")
print(classification_report(y_test_half, y_pred_half))

# Karışıklık matrisi
print("\nTam Maç Sonucu Karışıklık Matrisi:")
print(confusion_matrix(y_test_full, y_pred_full))

print("\nİlk Yarı Sonucu Karışıklık Matrisi:")
print(confusion_matrix(y_test_half, y_pred_half))

def tahmin_et(takim1, takim2):
    # Takımların indekslerini bul
    takim1_index = df_cleaned[df_cleaned['Ev Sahibi'] == takim1_transformed].index.values[0]
    takim2_index = df_cleaned[df_cleaned['Misafir'] == takim2_transformed].index.values[0]

    # Takımlar arasındaki maçın verilerini al
    maclar = df_cleaned.iloc[[takim1_index, takim2_index]]

    # Gerekli özellikleri seç ve standartlaştır
    X_pred = scaler.transform(maclar.drop(columns=['Maç Sonucu', 'Sonuç', 'İlk Yarı', 'İlk Yarı Sonuç']))

    # Tahminleri yap
    tahminler_full = rf_full_match.predict(X_pred)
    tahminler_half = rf_half_time.predict(X_pred)

    # Tahmin sonuçlarını harfe dönüştür
    tahmin_full_sonuc = le_result.inverse_transform(tahminler_full)[0]
    tahmin_half_sonuc = le_result.inverse_transform(tahminler_half)[0]

    return tahmin_full_sonuc, tahmin_half_sonuc

# Örnek olarak Fenerbahçe ve Galatasaray arasındaki maçın sonuçlarını tahmin edelim
takim1 = 'Konyaspor'
takim2 = 'Galatasaray'
takim1_transformed = le_home.transform([takim1])[0]
takim2_transformed = le_visitor.transform([takim2])[0]

tahmin_full, tahmin_half = tahmin_et(takim1, takim2)
print(f"{takim1} - {takim2} Maçı Tahmini (Tam Sonuç): {tahmin_full}")
print(f"{takim1} - {takim2} Maçı Tahmini (İlk Yarı Sonucu): {tahmin_half}")

def tahmin_et(takim1, takim2):
    # Takımların dönüştürülmüş adlarını bul
    takim1_transformed = le_home.transform([takim1])[0]
    takim2_transformed = le_visitor.transform([takim2])[0]

    # Takımlar arasındaki maçın verilerini al
    maclar = df_cleaned.loc[(df_cleaned['Ev Sahibi'] == takim1_transformed) & (df_cleaned['Misafir'] == takim2_transformed)]

    # Gerekli özellikleri seç ve standartlaştır
    X_pred = scaler.transform(maclar.drop(columns=['Maç Sonucu', 'Sonuç', 'İlk Yarı', 'İlk Yarı Sonuç']))

    # Tahminleri yap
    tahminler_full = rf_full_match.predict(X_pred)
    tahminler_half = rf_half_time.predict(X_pred)

    # Tahmin sonuçlarını harfe dönüştür
    tahmin_full_sonuc = le_result.inverse_transform(tahminler_full)[0]
    tahmin_half_sonuc = le_result.inverse_transform(tahminler_half)[0]

    # Tahmin edilen gol sayılarını al
    ev_sahibi_gol = maclar['Ev Sahibi Gol'].values[0]
    misafir_gol = maclar['Misafir Gol'].values[0]

    return tahmin_full_sonuc, tahmin_half_sonuc, ev_sahibi_gol, misafir_gol

# Örnek olarak Fenerbahçe ve Galatasaray arasındaki maçın sonuçlarını tahmin edelim
takim1 = 'Konyaspor'
takim2 = 'Galatasaray'

tahmin_full, tahmin_half, ev_sahibi_gol, misafir_gol = tahmin_et(takim1, takim2)
print(f"{takim1} - {takim2} Maçı Tahmini (Tam Sonuç): {tahmin_full}")
print(f"{takim1} - {takim2} Maçı Tahmini (İlk Yarı Sonucu): {tahmin_half}")
print(f"{takim1} - {takim2} Maçı Tahmini (Ev Sahibi Gol Sayısı): {ev_sahibi_gol}")
print(f"{takim1} - {takim2} Maçı Tahmini (Misafir Gol Sayısı): {misafir_gol}")