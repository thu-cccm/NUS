#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import os
import re
from pathlib import Path
import sys

def remove_comments(content, ext):
    """删除注释"""
    ext = ext.lower().lstrip('.')
    
    if ext in ['java', 'js', 'ts', 'jsx', 'tsx', 'vm']:
        # 删除多行注释
        content = re.sub(r'/\*[\s\S]*?\*/', '', content)
        # 删除单行注释
        lines = content.split('\n')
        result = []
        for line in lines:
            if '//' in line:
                idx = line.find('//')
                line = line[:idx].rstrip()
            if line.strip() or not result:
                result.append(line)
        content = '\n'.join(result)
    elif ext == 'vue':
        # 删除HTML注释
        content = re.sub(r'<!--[\s\S]*?-->', '', content)
        # 删除JS注释
        content = re.sub(r'/\*[\s\S]*?\*/', '', content)
        lines = content.split('\n')
        result = []
        for line in lines:
            if '//' in line:
                idx = line.find('//')
                line = line[:idx].rstrip()
            if line.strip() or not result:
                result.append(line)
        content = '\n'.join(result)
    elif ext == 'sql':
        # 删除多行注释
        content = re.sub(r'/\*[\s\S]*?\*/', '', content)
        # 删除单行注释
        lines = content.split('\n')
        result = []
        for line in lines:
            if '--' in line:
                idx = line.find('--')
                line = line[:idx].rstrip()
            result.append(line)
        content = '\n'.join(result)
    elif ext in ['css', 'scss']:
        # 删除CSS注释
        content = re.sub(r'/\*[\s\S]*?\*/', '', content)
    
    return content

def update_author(content):
    """更新作者信息"""
    content = re.sub(r'@author\s+[^\n]*', '@author maoshun tian', content, flags=re.IGNORECASE)
    content = re.sub(r'"author"\s*:\s*"[^"]*"', '"author": "maoshun tian"', content, flags=re.IGNORECASE)
    content = re.sub(r"'author'\s*:\s*'[^']*'", "'author': 'maoshun tian'", content, flags=re.IGNORECASE)
    return content

def process_file(file_path):
    """处理单个文件"""
    try:
        ext = file_path.suffix.lower()
        with open(file_path, 'r', encoding='utf-8') as f:
            original = f.read()
        
        content = remove_comments(original, ext)
        content = update_author(content)
        content = re.sub(r'\n{3,}', '\n\n', content)
        content = re.sub(r'^\n+', '', content)
        content = re.sub(r'\n+$', '\n', content)
        
        if content != original:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        return False
    except Exception as e:
        return f"ERROR: {str(e)}"

def main():
    base_dir = Path(__file__).parent
    extensions = ['.java', '.js', '.ts', '.vue', '.sql', '.scss', '.css', '.vm', '.json']
    exclude_dirs = {'node_modules', '.git', 'target', 'dist', 'build', '.idea', '.vscode', '__pycache__', '.mvn'}
    exclude_files = {'batch_process.py', 'fast_process.py', 'process_all_files.py', 'remove_comments_and_update_author.py'}
    
    files = []
    for root, dirs, filenames in os.walk(base_dir):
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        for filename in filenames:
            file_path = Path(root) / filename
            if file_path.suffix.lower() in extensions and file_path.name not in exclude_files:
                files.append(file_path)
    
    total = len(files)
    processed = 0
    changed = 0
    errors = 0
    
    print("=" * 70)
    print(f"开始处理 {total} 个文件")
    print("=" * 70)
    print()
    
    for i, file_path in enumerate(files, 1):
        result = process_file(file_path)
        if result is True:
            changed += 1
            print(f"[{i:4d}/{total}] ✓ {file_path.name}")
        elif result is False:
            pass
        else:
            errors += 1
            print(f"[{i:4d}/{total}] ✗ {file_path.name} - {result}")
        
        if i % 100 == 0:
            print(f"\n进度: {i}/{total} ({i*100//total}%) | 已修改: {changed} | 错误: {errors}\n")
            sys.stdout.flush()
    
    print()
    print("=" * 70)
    print(f"处理完成!")
    print(f"总计: {total} 个文件")
    print(f"已修改: {changed} 个文件")
    print(f"无变化: {total - changed - errors} 个文件")
    print(f"错误: {errors} 个文件")
    print("=" * 70)

if __name__ == '__main__':
    main()

